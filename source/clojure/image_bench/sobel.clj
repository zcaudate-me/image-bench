(ns image-bench.sobel
  (:require [uncomplicate.clojurecl.core :as cl]
            [uncomplicate.clojurecl.legacy :as legacy]
            [uncomplicate.commons.core :as common]
            [image-bench.image :as img]
            [image-bench.grayscale :as gray])
  (:import javax.imageio.ImageIO
           java.awt.image.DataBuffer
           java.awt.image.BufferedImage
           org.jocl.CL))

;; __kernel void sobel_uchar(__global const uchar * src, int src_step, int src_offset, int rows, int cols,
;;                           __global uchar* dst, int dst_step, int dst_offset)
(defn sobel-invalid-event-error
  ([input]
   (sobel-invalid-event-error input [1 1]))
  ([input local-size]
   (let [output (img/blank input)]
     (sobel-invalid-event-error input output (.getWidth input) (.getHeight input) local-size)
     output))
  ([input output width height]
   (sobel-invalid-event-error input output width height [1 1]))
  ([input output width height local-size]
   (common/with-release
     [dev (first (cl/devices (first (cl/platforms))))
      ctx (cl/context [dev])
      cqueue (legacy/command-queue-1 ctx dev)]
     (let [work-size (cl/work-size [width height] local-size)
           program-source (slurp "source/opencl/sobel.cl")]
       (common/with-release [prog    (cl/build-program! (cl/program-with-source ctx [program-source]))
                             p-src   (cl/cl-buffer ctx (* width height) :read-only)
                             p-dst   (cl/cl-buffer ctx (* width height) :write-only)
                             sobel-kernel (cl/kernel prog "sobel_uchar")
                             done    (cl/event)
                             events  (doto (make-array org.jocl.cl_event 1) (aset 0 done))]
         (cl/set-args! sobel-kernel
                       p-src
                       (int-array [width])
                       (int-array [0])
                       (int-array [height])
                       (int-array [width])
                       p-dst
                       (int-array [width])
                       (int-array [0]))
         (cl/enq-write! cqueue p-src (img/data-array input))
         (cl/enq-nd! cqueue sobel-kernel work-size done)
         (CL/clWaitForEvents 1 events)
         (cl/enq-read! cqueue p-dst (img/data-array output)))))))



;; __kernel void sobel_uchar(__global const uchar * src, int src_step, int src_offset, int rows, int cols,
;;                           __global uchar* dst, int dst_step, int dst_offset)
(defn sobel-invalid-work-group-error
  ([input]
   (sobel-invalid-work-group-error input [1 1]))
  ([input local-size]
   (let [output (img/blank input)]
     (sobel-invalid-work-group-error input output (.getWidth input) (.getHeight input) local-size)
     output))
  ([input output width height]
   (sobel-invalid-work-group-error input output width height [1 1]))
  ([input output width height local-size]
   (common/with-release
     [dev (first (cl/devices (first (cl/platforms))))
      ctx (cl/context [dev])
      cqueue (legacy/command-queue-1 ctx dev)]
     (let [work-size (cl/work-size [width height] local-size)
           program-source (slurp "source/opencl/sobel.cl")]
       (common/with-release [prog    (cl/build-program! (cl/program-with-source ctx [program-source]))
                             p-src   (cl/cl-buffer ctx (* width height) :read-only)
                             p-dst   (cl/cl-buffer ctx (* width height) :write-only)
                             sobel-kernel (cl/kernel prog "sobel_uchar")]
         (cl/set-args! sobel-kernel
                       p-src
                       (int-array [width])
                       (int-array [0])
                       (int-array [height])
                       (int-array [width])
                       p-dst
                       (int-array [width])
                       (int-array [0]))
         (cl/enq-write! cqueue p-src (img/data-array input))
         (cl/enq-nd! cqueue sobel-kernel work-size)
         (cl/enq-read! cqueue p-dst (img/data-array output)))))))


;; __kernel void sobel_uchar(__global const uchar * src, int src_step, int src_offset, int rows, int cols,
;;                           __global uchar* dst, int dst_step, int dst_offset)
(defn sobel-zero-output
  ([input]
   (sobel-zero-output input [1 1]))
  ([input local-size]
   (let [output (img/blank input)]
     (sobel-zero-output input output (.getWidth input) (.getHeight input) local-size)
     output))
  ([input output width height]
   (sobel-zero-output input output width height [1 1]))
  ([input output width height local-size]
   (common/with-release
     [dev (first (cl/devices (first (cl/platforms))))
      ctx (cl/context [dev])
      cqueue (legacy/command-queue-1 ctx dev)]
     (let [work-size (cl/work-size [width height] local-size)
           program-source (slurp "source/opencl/sobel.cl")]
       (common/with-release [prog    (cl/build-program! (cl/program-with-source ctx [program-source]))
                             p-src   (cl/cl-buffer ctx (* width height) :read-only)
                             p-dst   (cl/cl-buffer ctx (* width height) :write-only)
                             sobel-kernel (cl/kernel prog "sobel_uchar")
                             done    (cl/event)
                             events  (doto (make-array org.jocl.cl_event 1) (aset 0 done))]
         (cl/set-args! sobel-kernel
                       p-src
                       (int-array [width])
                       (int-array [0])
                       (int-array [height])
                       (int-array [width])
                       p-dst
                       (int-array [width])
                       (int-array [0]))
         (cl/enq-write! cqueue p-src (img/data-array input))

         (CL/clEnqueueNDRangeKernel cqueue
                                    sobel-kernel
                                    (.workdim work-size)
                                    (.offset work-size)
                                    (.global work-size)
                                    (.local work-size)
                                    0
                                    nil
                                    done)

         (CL/clWaitForEvents 1 events)           
         (cl/enq-read! cqueue p-dst (img/data-array output)))))))



(comment

  ;;
  ;;
  ;; can also try "resources/lena-0250x0250.jpg"
  (def eye (-> (img/load-image "resources/eye-0016x0016.jpg")
               (gray/grayscale)))
  
  (img/display-image eye)
  (println (img/grayscale-to-hex eye))


  ;;
  ;; using cl/event and cl/enq-nd!
  ;; 
  (def out1a (sobel-invalid-event-error eye [1 1])) 
  (println (img/grayscale-to-hex out1a)) ;; fine

  ;; throws OpenCL error: CL_INVALID_EVENT
  (def out1b (sobel-invalid-event-error eye [8 8]))


  ;;
  ;; using only cl/enq-nd!
  ;;   
  (def out2a (sobel-invalid-work-group-error eye [1 1]))
  (println (img/grayscale-to-hex out2a)) ;; fine

  ;; throw OpenCL error: CL_INVALID_WORK_GROUP_SIZE
  (def out2b (sobel-invalid-work-group-error eye [8 8]))
  

  ;;
  ;; using org.jocl.CL/clEnqueueNDRangeKernel
  ;;   
  (def out3a (sobel-zero-output eye [1 1]))
  (println (img/grayscale-to-hex out3a)) ;; fine

  (def out3b (sobel-zero-output eye [8 8]))
  (println (img/grayscale-to-hex out3b)) ;; Outputs are zero
)


(comment

  (def rainbow-color (img/load-image "resources/rainbow-0016x0016.jpg"))
  (println (img/color-to-hex rainbow-color))
  
  
  (def rainbow-gray (gray/grayscale rainbow-color))
  
  (println (img/grayscale-to-hex rainbow-gray))
  (img/display-image rainbow-gray)
  )

(comment

  
  (def lena-0500 (-> (img/load-image "resources/lena-0500x0500.jpg")
                     (gray/grayscale)))

  (img/display-image (sobel-zero-output lena-0500))
  
  (def lena-1000 (-> (img/load-image "resources/lena-1000x1000.jpg")
                     (gray/grayscale)))
  
  (img/display-image (img/sobel-zero-output lena)))
