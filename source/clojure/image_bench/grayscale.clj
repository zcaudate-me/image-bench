(ns image-bench.grayscale
  (:require [uncomplicate.clojurecl.core :as cl]
            [uncomplicate.clojurecl.legacy :as legacy]
            [uncomplicate.commons.core :as common]
            [image-bench.image :as img])
  (:import java.awt.image.BufferedImage
           image.bench.Util
           org.jocl.CL))


(defn ^BufferedImage grayscale-jvm
  ([^BufferedImage input]
   (let [output   (img/blank input)]
     (Util/grayscale input output)
     output))
  ([^BufferedImage input ^BufferedImage output]
   (Util/grayscale input output)))

(defn grayscale
  ([input]
   (grayscale input [1 1]))
  ([input local-size]
   (let [output (img/blank input)]
     (grayscale input output (.getWidth input) (.getHeight input) local-size)
     output))
  ([input output width height]
   (grayscale input output width height [1 1]))
  ([input output width height local-size]
   (common/with-release
     [dev (first (cl/devices (first (cl/platforms))))
      ctx (cl/context [dev])
      cqueue (legacy/command-queue-1 ctx dev)]
     (let [work-size (cl/work-size [width height] local-size)
           program-source (slurp "source/opencl/grayscale.cl")]
       (common/with-release [prog    (cl/build-program! (cl/program-with-source ctx [program-source]))
                             p-src   (cl/cl-buffer ctx (* 3 width height) :read-only)
                             p-dst   (cl/cl-buffer ctx (* width height) :write-only)
                             kernel (cl/kernel prog "grayscale_uchar")]
         (cl/set-args! kernel
                       p-src
                       (int-array [width])
                       (int-array [0])
                       (int-array [height])
                       (int-array [width])
                       p-dst
                       (int-array [width])
                       (int-array [0]))
         (cl/enq-write! cqueue p-src (img/data-array input))

         (cl/enq-nd! cqueue kernel work-size)
         #_(CL/clEnqueueNDRangeKernel cqueue
                                    kernel
                                    (.workdim work-size)
                                    (.offset work-size)
                                    (.global work-size)
                                    (.local work-size)
                                    0
                                    nil
                                    nil)
         
         (cl/enq-read! cqueue p-dst (img/data-array output)))))))


(comment
  (def lena (img/load-image "resources/lena-0500x0500.jpg"))

  (def lena-gray (grayscale lena [8 8])))
