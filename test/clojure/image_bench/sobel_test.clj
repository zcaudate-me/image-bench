(ns image-bench.sobel-test
  (:use hara.test)
  (:require [image-bench.sobel :as sobel]
            [image-bench.grayscale :as gray]
            [image-bench.image :as img]))


;; PASSES
(fact "running a sobel kernel at [345, 912] [1 1]"

  (def img (img/load-image "resources/lena-0345x0912.jpg"))

  (gray/grayscale img)
  => java.awt.image.BufferedImage)


;; FAILS
(fact "running a sobel kernel at [345, 912] [13, 3]"

  (def img (img/load-image "resources/lena-0345x0912.jpg"))

  (gray/grayscale img [13 3])
  => java.awt.image.BufferedImage)
