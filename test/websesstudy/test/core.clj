(ns websesstudy.test.core
  (:use [websesstudy.core] [websesstudy.session])
  (:use [clojure.test])
  (:import [java.util UUID]))


(def sid (str (UUID/randomUUID)))


(def request {:session {:sid sid}})


(defn init-request []
  (def request {:session {:sid sid}}))


(defn init-counter [request]
  (session-set! request :count 0))


(deftest test-get-counter
  (is (= 0 (get-counter request)) "Get counter should return 0 if there is no counter initialized before.")
  (counter request)
  (is (= 1 (get-counter request)) "Get counter should return 1 after calling counter.")
  (counter request)
  (is (= 2 (get-counter request)) "Should be 2 because get counter function is not allowed to increment count."))


(deftest test-counter
  (init-counter request)
  (is (= 1 (counter request)) "Counter function increments value by 1 and return current value."))


(deftest test-current-tasks ;; FIXME: write
  (is false "No tests have been written."))


(deftest test-add-task ;; FIXME: write
  (is false "No tests have been written."))


(deftest test-tasks ;; FIXME: write
  (is false "No tests have been written."))


(deftest test-home ;; FIXME: write
  (is false "No tests have been written."))


(deftest test-index ;; FIXME: write
  (is false "No tests have been written."))


(deftest test-run ;; FIXME: write
  (is false "No tests have been written."))