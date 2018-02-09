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
  
  :profiles {:dev {:dependencies [[zcaudate/hara.object "2.8.1"]
                                  [zcaudate/hara.test "2.8.1"]
                                  [zcaudate/lucid.mind "1.4.0"]
                                  [zcaudate/lucid.package "1.4.0"]
                                  [zcaudate/lucid.publish "1.4.0"]
                                  [zcaudate/lucid.unit "1.4.0"]
                                  [zcaudate/lucid.core.inject "1.4.0"]]
  		              :plugins [[lein-ancient "0.6.15"]
                              ;[lein-virgil "0.1.7"]
                              ]}}

  :injections  [(require '[lucid.core.inject :as inject]
                         'hara.test
                         'hara.object
                         'lucid.unit
                         'lucid.publish
                         'lucid.package)
                (inject/in [lucid.core.inject :refer [inject [in inject-in]]]
                           [lucid.package pull]
                           [hara.io.project project]
                           [hara.object to-map to-data]
   
                           clojure.core
                           [lucid.mind .& .> .? .* .% .%> .>var .>ns])]
  :java-source-paths ["source/java"]
  :source-paths ["source/clojure"]
  :test-paths ["test/clojure"])
