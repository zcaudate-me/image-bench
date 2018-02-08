# image-bench

This is a project to explore the differences and similarities of using OpenCL through Clojure and C++.

## Dependencies

- OpenCV (brew install opencv)
- Leiningen (brew install leiningen)
- CMake (brew install cmake)

## Usage

C++:
  
    ./run.sh
  
This will compile the code and run `bin/sobel_benchmark`, showing outputs for the sobel transform on different sized images for different local workgroup size:

    --------------------------------------
    SOBEL TRANSFORM (1, 1)
    --------------------------------------
    0250x0250:          12.117 ms
    0500x0500:          39.846 ms
    1000x1000:          20.853 ms
    2000x2000:          72.800 ms
    --------------------------------------
    --------------------------------------
    SOBEL TRANSFORM (1, 8)
    --------------------------------------
    0250x0250:          6.753 ms
    0500x0500:          21.825 ms
    1000x1000:          5.003 ms
    2000x2000:          19.693 ms
    --------------------------------------
    --------------------------------------
    SOBEL TRANSFORM (8, 8)
    --------------------------------------
    0250x0250:          1.208 ms
    0500x0500:          2.083 ms
    1000x1000:          1.670 ms
    2000x2000:          4.460 ms
    --------------------------------------
    --------------------------------------
    SOBEL TRANSFORM (32, 8)
    --------------------------------------
    0250x0250:          0.977 ms
    0500x0500:          1.480 ms
    1000x1000:          1.674 ms
    2000x2000:          4.194 ms
    --------------------------------------
    
  
Clojure

    lein repl

There is currently a bug in [image-bench.sobel](https://github.com/zcaudate-me/image-bench) where setting local workgroup size to more than [1 1] will cause errors. This is reproducible by following the [comment block](https://github.com/zcaudate-me/image-bench/blob/master/source/clojure/image_bench/sobel.clj#L196-L237) in the source code:

```clojure
(use 'image-bench.sobel)
(require '[image-bench.core :as img]
         '[image-bench.grayscale :as gray])

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
(println (img/grayscale-to-hex out3a));; fine

(def out3b (sobel-zero-output eye [8 8]))
(println (img/grayscale-to-hex out3b)) ;; Outputs are zero
```
## License

Copyright © 2018 Chris Zheng

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
