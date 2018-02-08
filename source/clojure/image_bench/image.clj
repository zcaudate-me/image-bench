(ns image-bench.image
  (:require [hara.io.encode.hex :as hex]
            [clojure.java.io :as io]
            [clojure.string :as string])
  (:import javax.imageio.ImageIO
           java.awt.image.DataBufferByte
           java.awt.image.DataBuffer
           java.awt.image.BufferedImage
           java.awt.image.Raster
           javax.swing.ImageIcon
           image.bench.Util))
           
(defn display-image [^BufferedImage img]
  (doto (javax.swing.JFrame.)
    (-> (.getContentPane)
        (doto
            (.setLayout (java.awt.FlowLayout.))
          (.add (javax.swing.JLabel. (javax.swing.ImageIcon. img)))))
    (.pack)
    (.setVisible true)))

(defn data-array [^BufferedImage img]
  (-> img
      (.getRaster)
      (.getDataBuffer)
      (.getData)))

(defn ^BufferedImage load-image [path]
  (ImageIO/read (io/file path)))

(defn ^BufferedImage blank [^BufferedImage input]
  (let [output (BufferedImage. (.getWidth input)
                               (.getHeight input)
                               BufferedImage/TYPE_BYTE_GRAY)]
    output))

(defn color-to-hex
  ([img]
   (let [rows   (.getHeight img)
         cols   (.getWidth img)
         arr    (data-array img)]
     (string/join "\n"
                  (for [y (range rows)]
                    (string/join " "
                                 (for [x (range cols)]
                                   (let [i (* 3 (+ (* y cols) x))]
                                     (.toUpperCase (str (apply str (hex/hex-chars (aget arr i)))
                                                        (apply str (hex/hex-chars (aget arr (inc i))))
                                                        (apply str (hex/hex-chars (aget arr (inc (inc i)))))))))))))))

(defn grayscale-to-hex [img]
  (let [rows   (.getHeight img)
        cols   (.getWidth img)
        arr (data-array img)]
    (string/join "\n"
                 (for [y (range rows)]
                   (string/join " "
                                (for [x (range cols)]
                                  (let [i (+ (* y cols) x)]
                                    (.toUpperCase (apply str (hex/hex-chars (aget arr i)))))))))))

(defn hex-to-color [hex rows cols]
  (let [img (BufferedImage. cols rows BufferedImage/TYPE_3BYTE_BGR)
        hex  (.replaceAll hex "\\s" "")
        data (Util/hexStringToByteArray hex)]
    (doto img
      (.setData (Raster/createRaster (.getSampleModel img)
                                     (DataBufferByte. data (count data))
                                     nil)))))

(defn hex-to-grayscale [hex rows cols]
  (let [img (BufferedImage. cols rows BufferedImage/TYPE_BYTE_GRAY)
        hex  (.replaceAll hex "\\s" "")
        data (Util/hexStringToByteArray hex)]
    (doto img
      (.setData (Raster/createRaster (.getSampleModel img)
                                     (DataBufferByte. data (count data))
                                     nil)))))

(comment

  (def in (load-image "resources/eye-0016x0016.jpg"))
  (def hex (color-to-hex in))
  (def out (hex-to-color hex 16 16))  
  (display-image out)

  )

(comment

  (def in (load-image "resources/lena-0500x0500.jpg"))
  (def hex (color-to-hex in))
  (def out (hex-to-color hex 500 500))
  (display-image out)

  )

(comment

  (def hex (slurp "resources/rainbow-0016x0016.txt"))
  (def out (hex-to-color hex 16 16))
  (display-image out)

  (format "%3d" 1)
  )

(comment

  (require '[image-bench.sobel :as sobel]
           '[image-bench.grayscale :as grayscale])
  (def hex (slurp "resources/lena-1000x1000.txt"))
  ;;(Util/hexStringToByteArray hex)
  (def input (hex-to-color hex 1000 1000))
  (def input-gray (grayscale/grayscale input))
  (def output (sobel/sobel-zero-output input-gray))
  (display-image output)
  )


(comment

  (require '[image-bench.sobel :as sobel]
           '[image-bench.grayscale :as grayscale])
  (def hex (slurp "resources/eye-0032x0032.txt"))
  (def img (hex-to-color hex 32 32))
  (def img-gray (grayscale/grayscale img))
  (def img-sobel (sobel/sobel-zero-output img-gray))

  (println "COLOR:\n" (color-to-hex img) "\n")

  (println "GRAYSCALE:\n" (grayscale-to-hex img-gray) "\n")

  (println "SOBEL:\n" (grayscale-to-hex img-sobel) "\n")
  )
