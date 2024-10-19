(ns core
  (:require
    [jsonista.core :as jsonista]
    [reitit.ring :as ring]
    [ring.adapter.jetty :refer [run-jetty]]
    [taoensso.telemere :as tele]))

(assert
  (= (get (tele/check-interop) :slf4j)
     {:present?                   true
      :telemere-provider-present? true
      :sending->telemere?         true
      :telemere-receiving?        true}))

(def json-handler
  (tele/handler:console
    {:output-fn (tele/pr-signal-fn
                  {:pr-fn     jsonista.core/write-value-as-string
                   :incl-kvs? true})}))

(defn setup-logging
  []
  (tele/streams->telemere!)
  (tele/add-handler! :default/console json-handler)) ; {:ns-filter {:disallow "org.eclipse.jetty.server.*"}}))


(def app
  (ring/ring-handler
    (ring/router
      ["/ok"
       {:get (fn [_]
               {:status 200
                :body   "ok"})}])))

(defonce server (atom nil))

(defn start!
  []
  (reset! server (run-jetty
                   #'app
                   {:port  8080
                    :join? false})))

(defn -main
  []
  ; (setup-logging)
  (start!)
  (prn "server started"))
