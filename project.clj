(defproject archie "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.494"]
                 [cheshire "5.3.1"]
                 [compojure "1.5.1"]
                 [com.novemberain/monger "3.1.0"]
                 [clj-http "2.3.0"]
                 [cljs-http "0.1.42"]
                 [cheshire "5.7.0"]
                 [com.cognitect/transit-cljs "0.8.239"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.4"]
                 [ring/ring-json "0.3.1"]
                 [ring.middleware.logger "0.5.0"]
                 [ring/ring-servlet "1.2.0-RC1"]
                 [ring/ring-defaults "0.2.1"]]
  :plugins [[lein-ring "0.9.7"]
           [lein-cljsbuild "1.1.1"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {
              :builds [{:source-paths ["src"]
                        :compiler {:output-to "resources/public/js/main.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}]}
  :ring {:handler archie.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
