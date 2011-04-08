# websesstudy

Clojure web application session study


## Background

This is meant for the study of the first of my Clojure web application component namely user session manager. After having hard time to understand programming language itself I was struggling with session management the way I was used to use it before. Simple set key and value of almost any kind, set flash message(s) and pull them on next page load, having counters etc.

While sessions on PHP are used from file system by default and the session variable ($_SESSION) is request and user scoped by default, I had to do much more work on Compojure web application to have same functionality. I had to create an atom map having keys made by session ids that every user carries over the page requests. Ring session middleware already have some of the functionality, probably the most because it can have options passed on handler, but I didn’t find necessary information how to use it, so in my approach I just use ring session wrapper to initialize session key on request and then I use it by my own functionality.

Since this is my first Clojure program, I rather leave this as a study case and open for improvements and maybe throwing away if and when I find better, more straightforward way to do this. But for now, it work as I need it on my other project. Study is create on MacOSX, IntelliJ IDEA and deployed project tested on Ubuntu Linux. Commands can differ a bit when using Windows.

### Newbies

I have a feeling a lot of users will have difficulties to get into the Clojure application development cycle just because of lack of the very basic instructions. Well this is what I personally felt, so this is why I recommend to get an overview of how things work in Clojure world: [Inside Clojure](http://channel9.msdn.com/shows/Going+Deep/Expert-to-Expert-Rich-Hickey-and-Brian-Beckman-Inside-Clojure/). Then if you want to have closer look to the language I recommend this brief but comprehensive [Intro to Clojure](http://java.ociweb.com/mark/clojure/article.html#Intro)

From PHP world its best to prepare on rather steep learning curve, because at the first, a lot of things seems much more complicated and even frustrating compared you can just put `<?="Hello world"?>` on file on any PHP enabled server and have your first web application running.

But at some point you may get interested how things actually work on deeper level and especially you may question, if some things are more sophisticated on other languages or platforms. My interest rose when I was designing flexible form creation and handling tool for prototyping. The work involved between configuration, object mapping and business logics seemed too much overlapping.

## Usage

Before actual steps you need to have Clojure and Lein installed on your computer. Easiest way I know is to install [this script](https://github.com/technomancy/leiningen/raw/stable/bin/lein) to you bin directory and simply run it. More instructions are found from: https://github.com/technomancy/leiningen

After knowing how things work on Clojure you shouldn't find next few steps too difficult to follow and replicate my study case: 

1. Create a project with `$ lein new "your-application-name"`. Please note you don’t need to download package from github at all when you do this. My first mistake with first Clojure experiment was to download package, then use lein and wonder what happened! Here you just use my libraries to create your own application starting from your application name. Ok, you could download github code and run my app by leaving step 1 and 2 off from the routine just starting from step 3. But then at the end, this is how most of the Clojure installations are instructed and you should get familiar with it because common development and deployment process relies heavily on it.

2. Modify your project.clj with text editor to include `[websesstudy "1.0.0-SNAPSHOT"]` on dependencies. You can see [comment part](https://github.com/mmstud/websesstudy/blob/master/project.clj) from github project file how it goes and where it goes. Please note the other part of the project file looks different, more simple on a new project.

3. Run `$ lein deps`. This downloads all necessary files over the Internet. Please note, that lein command needs read and write permissions on your project directory so if you counter problems on downloading, most probably you need to add write rights to lib and other directories involved. Other option is to run `$ sudo lein deps` to overcome this problem.

After getting all necessary libraries you can run `$ lein test` or `$ lein repl` and experiment with the code. Lein test evidently just checks over my codes with my own tests, so result isn’t that interesting, but you can perhaps learn some basics from test codes anyway.

### Getting on browser

Running repl is one of the key activities on LISP like language application development. Its in a way same than running PHP on interactive mode from command line, but some would say its totally different. Anyway you can experiment my study case by running a web server and see how sessions are working on your local browser.

4. Run `$ lein repl`

5. Next thing you need to do is load websesstudy core library: `(use 'websesstudy.core)`.

6. Finally you need to evaluate server run function: `(run)` and point your browser to `http://localhost:8080/` to see the example application running on your local machine. Press ctrl-c to stop the server.

And this is the end of the study case.

## License

Copyright (C) 2011 Marko Manninen

Distributed under the Eclipse Public License, the same as Clojure.