(ns websesstudy.session
    (:use [ring.middleware.session])
    (:import [java.util UUID]))

; memory store for session handling

; TODO flush time per user session: 2h?
(def *STORE* (atom {}))

(def *empty-flash* "")

; without parameter returns current user session map
; pass map, returns new user session map, so this is the way to change
; all keys at once on session store. use setter to change only one key value pair
(defn session-bind
  ([sid] (session-bind sid (get @*STORE* sid {})))
  ([sid session]
    (swap! *STORE* assoc sid session)
    (get @*STORE* sid)))

; set user session id. should be called only once from sid-get because this
; initializes user session store with sid value only
(defn- sid-set! [sid]
    (session-bind sid {:sid sid}) sid)

; get or generate new session id with java UUID generator
(defn sid-get
    ([] (sid-get {}))
    ([request] 
    (let [sid (get (get request :session {}) :sid "")]
        (if (= "" sid)
            (sid-set! (str (UUID/randomUUID)))
            sid))))

; get, set, del and flush for user session store

; get session value by key. in case no key is found, default return value
(defn session-get
    ([request key] (session-get request key ""))
    ([request key default-value]
    (get (session-bind (sid-get request)) key default-value)))

; returns whole user session
(defn- session-del! [sid key]
    (session-bind sid (dissoc (session-bind sid) key)))

; returns whole user session
(defn session-set! [request key val]
    (let [sid (sid-get request)]
        (session-del! sid key)
        (session-bind sid (merge {key val} {:sid sid} (session-bind sid)))))

; flush user session store
(defn session-flush! [request]
    (session-bind (sid-get request) {}))

; init and erase whole session store
(defn session-store-erase! []
    (def *STORE* (atom {})))

; flash message support between two requests. internal functions. use flash only
(defn- flash-get  [request] (session-get request :flash *empty-flash*))
(defn- flash-set! [request message] (session-set! request :flash message))
(defn- flash-del! [request] (session-del! (sid-get request) :flash))

; short version to be used both setting and getting flash message
; TODO multiple flash messages each one having status (:ok :notice  :warning :error) and message
; then its up to ui how to present flash message(s)
(defn flash
    ([request] (flash request *empty-flash*))
    ([request message]
        (if (= message *empty-flash*)
            (let [message (flash-get request)]
                (flash-del! request) message)
            (flash-set! request message))))

; gets session id and user session store from request
; if not found create a new and save both to request and response
; to include store right from first paage load / request thru out the application
(defn- wrap-session-bind+ [handler]
    (fn [request]
        (let [sid (sid-get request)
              request (update-in request [:session] merge {:sid sid})
              response (handler request)]
            (merge response {:session (session-bind sid)}))))

; originally wrap-session-bind was used with ring wrap-session:
; (def app (wrap-session (wrap-session-bind handler)))
; or with -> macro (def app (-> handler wrap-session-bind wrap-session))
; but here we wrap it all in once to have a cleaner call on main application
(defn wrap-session-bind [handler]
    (-> handler
        wrap-session-bind+
        wrap-session))