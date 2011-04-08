(ns websesstudy.core
  (:use
	  [compojure.core]
	  [hiccup.core]
	  [ring.adapter.jetty]
	  [ring.handler.dump]
	  [ring.util.response :only [redirect]])
  (:require
	  [compojure.route :as route]
	  [websesstudy.session :as ses]))

; get counter function
(defn get-counter [request]
	(ses/session-get request :count))

; add counter function. increment by one everytime. start from 0
(defn counter [request]
	(ses/session-set! request :count 
		(inc (ses/session-get request :count 0))))

; home html generator
(defn home [request content]
	; get possible flash message
	(let [message (ses/flash request)]
	(html [:div.page {:style "padding: 10px"}
			; create example application links
			[:a {:href "/dump"} "dump request"] [:br]
		  	[:a {:href (str "/add/my task " (get-counter request))} "add task"] [:br]
		  	[:a {:href "/tasks"} "tasks"] [:br]
		  	[:a {:href "/session/example-key/example value"} "set session key"]
				[:span " format: /session/{any-key}/{any-value}"] [:br]
		  	[:a {:href "/session/example-key"} "show session key"]
		 		[:span " format: /session/{any-key}"] [:br] ]
		  [:hr]
		  ; print possible flash message and other page content
		  [:div (if (not= ses/*empty-flash* message)
			  		[:div.flash {:style "border: dotted 1px #999; padding: 10px; color: green"} message])
          			[:div.content {:style "padding: 10px"} content]])))

; get current tasks
(defn current-tasks [request]
	(get (ses/session-bind (ses/sid-get request)) :tasks []))

; view current tasks
(defn view-tasks [request]
	(home request (str "tasks: " (current-tasks request))))

; add a task
(defn add-task [request task]
	(ses/session-set! request :tasks (merge (current-tasks request) task)))

; define all necessary routes for our example.
(defroutes index
  
  ; ANY route is called every page request.
  ; :next key just tells handler to continue and use other routes.
  (ANY "*"					request (counter request) :next)

  ; this route prints the home page having all links available related to this example.
  (GET "/" 		    		request (home request ""))

  ; this route prints out the whole content of request. Especially note ring-session, cookie and session keys on output.
  (GET "/dump" 		    	request (home request (handle-dump request)))

  ; this route returns the current value of key from session.
  (GET "/session/:key" 	    request (home request 
										  (str "Key [" (:key (:params request)) "] value: " 
											   (ses/session-get request (:key (:params request))))))

  ; this route sets value by key on session. overrides the former one. Setting it creates a new flash message.
  (GET "/session/:key/:val" request (do (str (ses/session-set! request (:key (:params request)) (:val (:params request))))
										(ses/flash request (str "Session key [" (:key (:params request)) "] set"))
										(redirect (str "/session/" (:key (:params request))))))

  ; this route shows tasks on session list.
  (GET "/tasks" 			request (view-tasks request))

  ; this route sets up a new task defined on url lie: /atask/My task #1. Creating a new one sets also the flash message.
  (GET "/add/:task"			request (do (add-task request (:task (:params request)))
										(ses/flash request "Task added")
										(redirect "/tasks")))

  ; this is a default route for all the other page requests. note, that counter is still running even this route is executed.
  (route/not-found "Page not found"))

; define application and use session wrapper middleware to enghange basic functionality for our purpose.
(def app (-> index
			 ses/wrap-session-bind))

; run server from $lein repl. press crtl-c to stop server.
(defn run []
	(run-jetty app {:port 8080}))