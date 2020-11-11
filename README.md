# R2DBC demo for Reactive Amsterdam

Running:

Start the Postgres movie database:
```
docker-compose up
```

Build and run the Spring application:
```
gradle bootRun
```

Playing with it:
```
curl localhost:8080/films
```
(if you have jq installed, JSON is more readable like this)
```
curl localhost:8080/films | jq
```
Run the combined query:
```
curl localhost:8080/filmsWithActors | jq  
```
This one queries all movies (1000 movies), for each movie queries all actors (avg. about 6), for each
actor it queries their name.
So in total it is about 7000 queries, runs in about 11 seconds on my machine.
(Note: I am fully aware this is a silly way to use a relational database ;-)