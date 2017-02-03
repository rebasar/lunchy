# Lunchy - A Scraper for lunch menus

This is a simple web application that fetches the lunch menus from restaurants close to my office and serves them as JSON so that they can be easily used by other tools like team chat adapters or command line clients. Creating a new parser is trivial thanks to excellent [jsoup](https://jsoup.org/) library. 

## Building

Just run `./mvnw package` and run the fat jar under the `target` directory

## Running

`java -jar target/lunchy-${version}.jar server config.yaml`

The config is needed because the Silvis menu takes more than 500ms (the Dropwizard default) to reply, so requires some configuration.

## Why infinispan?

Infinispan is a huge overkill for this application (it raises the jar size by 5 megabytes). However I wanted to be able to specify different lifetimes for each cached item. Since I didn't want to implement my own cache, Infinispan was the only option which provided this functionality. 

## Todo

- Fix the dependency injection so that any ParserService can be injected to cache
- Add more restaurants