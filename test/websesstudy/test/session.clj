(ns websesstudy.test.session
  (:use [websesstudy.session])
  (:use [clojure.test])
  (:import [java.util UUID]))


(def sid (str (UUID/randomUUID)))


(def request {:session {:sid sid}})


(defn init-sid []
  (def sid (str (UUID/randomUUID))))


(defn init-request []
  (def request {:session {:sid sid}}))


(defn init-store []
  (session-store-erase!))


(defn init-test []
  (init-sid)
  (init-request)
  (init-store))


(deftest test-session-bind
  (init-test)
  (is (= (session-bind sid) {}) "Session store should be empty without optional parameter.")
  (init-test)
  (is (= (session-bind sid {}) {}) "Session store should be empty with optional parameter.")
  (init-test)
  (is (= (session-bind sid {:sid sid}) {:sid sid}) "Session store should have only sid key and value.")
  (is (= (session-bind sid) {:sid sid}) "Session store should still have sid key and value."))


(deftest test-sid-get ;; TODO: when request doesnt have sid, create a new
  (init-test)
  (is (= (sid-get request) sid) "Session id on request"))


(deftest test-session-get
  (init-test)
  (is (= "" (session-get request :key)) "Session key not available. Default \"\".")
  (is (= "na" (session-get request :key "na")) "Session key not available with default value na.")
  (session-bind sid {:sid sid})
  (is (= sid (session-get request :sid)) "Session key not available."))


(deftest test-session-set!
  (init-test)
  (is (= (session-set! request :key "value") {:sid sid :key "value"}) "No tests have been written."))


(deftest test-session-flush!
  (init-test)
  (let [message "flash message"]
    (flash request message)
    (is (= message (session-get request :flash)) "Session store should have flash message.")
    (session-flush! request)
    (is (= *empty-flash* (session-get request :flash *empty-flash*)) "Session store should not have flash message.")))


(deftest test-session-store-erase!
  (init-test)
  (is (= @*STORE* {}) "Erasing all from session store"))


(deftest test-flash
  (init-test)
  (let [message "flash message"]
    (flash request message)
    (is (= message (flash request)) "Initialized and returned flash value check")))


;(deftest test-wrap-session-bind ;; FIXME: how to test?
;  (init-test)
;  (is false "No tests have been written."))