# Shopify Backend Challenge: Music Album Repository

My submission for the Shopify backend challenge is an album repository written in Java Spring Boot.
While it is far from the most complex backend I have created, I feel it is enough to highlight the my understanding of best practices
and the 'cleanliness' of my code. I also decided to write unit tests for the backend, as I feel that is an essential skill.

The concept for this app is to have an interface that a music record collector could use to visually
keep track of all their albums, and be able to search based on their custom descriptions and genre tags.
You can upload albums along with cover art, genre tags, a title, artist, and description,
and then search for albums by genre (match any / match all), or by keyword. You can also delete them.

The uploaded pictures are stored in the in-memory database as a byte array.
I'd like to emphasize I know this is not a scaleable solution; normally I'd store them on an AWS S3 bucket.
However, part of being a good developer is understanding when / where certain things are necessary, and I thought
storing them like this is sufficient for this small project 😉 .

I also made a front end using Vue.js to interface with the backend. Since this was a backend challenge, 
I took the liberty of reusing some components from a previous project of mine for the front end, and hooking
them up to my brand new backend!

Here are some screenshots, scroll down for how to use + more.

![github-small](https://github.com/JDziewonski98/albumrepo/blob/master/pics/mainpage.PNG)
The Main page.

![github-small](https://github.com/JDziewonski98/albumrepo/blob/master/pics/createnew.PNG)
Creating a new posting.

![github-small](https://github.com/JDziewonski98/albumrepo/blob/master/pics/exactmatch.PNG)
Matching albums that have ALL the selected genres

![github-small](https://github.com/JDziewonski98/albumrepo/blob/master/pics/textsearchnew.PNG)
Matching albums based on text

# How to use

To run the backend:

Option A)
If you have IntelliJ installed, open this repo as an IntelliJ project, then simply build and run the main
in the src folder.
You can right click on the tests folder and 'run all' to run tests.

Option B)
Ensure you have Gradle installed, then run the command 'gradle bootRun' from this root directory from the terminal.


To run the frontend:

'cd' into the 'frontend' folder and follow the instructions to run it in the readme within.

# Ideas for future
A few more ideas that I could definitely implement, given more time.
- Paginate api responses
- Set up Flyway to create database migrations, and to prepopualte with some data.
- Create a login authentication system to see just your albums.
- Add ability to edit your albums.
- Add frontend feature to select multiple and delete.
- Add custom tags other than genre.

