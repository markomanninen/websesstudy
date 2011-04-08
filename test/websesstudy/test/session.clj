(ns websesstudy.test.session
  (:use [websesstudy.session])
  (:use [clojure.test])
  (:import [java.util UUID]))

(def sid (str (UUID/randomUUID)))

(def request {:session {:sid sid}})

(deftest test-session-bind ;; FIXME: write
  (is false "No tests have been written."))

(deftest test-sid-get ;; FIXME: write
  (is false "No tests have been written."))

(deftest test-session-get ;; FIXME: write
  (is false "No tests have been written."))

(deftest test-session-set! ;; FIXME: write
  (is false "No tests have been written."))

(deftest test-session-flush! ;; FIXME: write
  (is false "No tests have been written."))

(deftest test-session-store-erase! ;; FIXME: write
  (is false "No tests have been written."))

(deftest test-flash
  (let [message "flash message"]
    (flash request message)
    (is (= message (flash request)) "Initialized and returned flash value check")))

(deftest test-wrap-session-bind ;; FIXME: write
  (is false "No tests have been written."))