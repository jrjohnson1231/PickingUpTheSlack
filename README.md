# archie

A Slack archiving service. Can be found on Heroku at http://secret-ridge-50925.herokuapp.com/chathistory.htm. archie is built in Clojure, and is based off of a Slack event subscription for the ND-CSE team. Slack sends events for any message in a public channel to archie, and he saves the information in a MongoDB instance. We also have build a RESTful API and a frontend that shows all of the archived messages. One unique feature we added is tagging. Any words that are prefixed with '$' are saved as a tag. These tags can then be looked up to show specific messages for that channel, allowing users to easily lookup conversations relating to certain topics like homework.

## Clojure

Clojure is a functional language that runs on the JVM. The concepts of functional languages aligns nicely with REST, because pure functions cannot possibly change the overall state of the application. It also worke really well for our database queries because all of the functions were atomic and if one failed the application did not suffer. Functional programming also made parsing the requests incredibly simple.

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running
The frontend runs best locally (Heroku limits the number of threads which is not ideal for Clojure).

To start a web server for the application, run:

    lein ring server $PORT # opens browser
    lein ring server-headless $PORT # doesn't open browser
    
Then neavigate to localhost:PORT/chathistory.htm. Depending on your browser, the messages may not be sorted properly. We have found that Firefox always works however.

## License

Copyright © 2017 FIXME
