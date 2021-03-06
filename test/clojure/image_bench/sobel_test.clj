(ns image-bench.sobel-test
  (:use hara.test)
  (:require [image-bench.sobel :as sobel]
            [image-bench.grayscale :as gray]
            [image-bench.image :as img]))


;; PASSES
(fact "running a grayscale kernel at [345, 912] [1 1]"

  (def img (img/load-image "resources/lena-0345x0912.jpg"))

  (gray/grayscale img)
  => java.awt.image.BufferedImage)

;; PASSES
(fact "running a grayscale kernel at [1000 1000] [4 4]"

  (def img (img/load-image "resources/lena-1000x1000.jpg"))

  (gray/grayscale img [4 4])
  => java.awt.image.BufferedImage)


;; FAILS
(fact "running a grayscale kernel at [345, 912] [15, 3]"

  (def img (img/load-image "resources/lena-0345x0912.jpg"))
  
  (gray/grayscale img [15 3])
  => java.awt.image.BufferedImage)

;; PASSES
(fact "running a grayscale kernel at [345, 912] [1 1]"

  (def img (img/load-image "resources/lena-0345x0912.jpg"))

  (sobel/sobel-invalid-work-group-error img)
  => java.awt.image.BufferedImage)


;; FAILS
(fact "running a sobel kernel at [345, 912] [15, 3]"

  (def img (img/load-image "resources/lena-0345x0912.jpg"))

  (sobel/sobel-invalid-work-group-error img [15 3])
  => java.awt.image.BufferedImage)
