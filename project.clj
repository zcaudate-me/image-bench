(defproject image-bench "0.1.0"
  :description "benchmarking opencl on different platforms"
  :url "https://gitlab.com/zcaudate/image-bench"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [uncomplicate/clojurecl "0.8.0"]
                 ;;[org.bytedeco/javacv "1.4"]
                 ;;[org.bytedeco/javacv-platform "1.4"]
                 ;;[org.bytedeco.javacpp-presets/opencv "3.4.0-1.4"]
                 ;;[org.bytedeco.javacpp-presets/opencv "3.4.0-1.4" :classifier "macosx-x86_64"]
                 ;;[org.bytedeco.javacpp-presets/opencv-platform "3.4.0-1.4"]
                 ;;[com.twelvemonkeys.imageio/imageio-tiff "3.3.2"]
                 ]
                 
  :aliases {"test" ["run" "-m" "hara.test" "exit"]}
  :java-source-paths ["source/java"]
  :source-paths ["source/clojure"]
  :test-paths ["test/clojure"])
