**Final Retrospective group 3+3=7**

We managed to deliver most of our core functionalities. Compared to our original proposal, we added data visualization to our project and we also added some nice to have like notification system, heatmaps, marker clusters and form auto complete. We did not implement the planning routes and comment incidents features. 


**Challenges**: 

The deployment took us a long time to figure out. It involved the deployment and also switching our database from SQLite to PostgreSQL.There were a lot of details to fine tune which required substantial knowledge that none of us had at first. We experimented with it with a lot of effort and consulted our advisor. We did get every detail right at the end and deployed successfully.  

We were having trouble reading data from the database and send it back to the frontend in the first place. We tried sending different kinds of request, jquery/ajax etc but none of those worked. I then realized that the model view controller actually puts data back in the vm file and we can access the data using .innerHTML. And we just parse the data we get from the query and convert it to json format and send it to the front end. We then were able to use the data to add persistent markers on the map,do visualizations and heatmaps.

Another challenge we encountered was implementing the notification system. We decided to go with email and used gmail with smtp at first, but gmail does not work well heroku. It took us a long time and a lot of trial and error to find a usable heroku emailing add-on. We consulted both official documents and external sources. At the end, we managed to make the emailing function work and integrate it into our app as the core of our notification system.

If we could go back and start at iteration 1, we would write a more detailed srs of how our app would look like. We would start earlier and make use of the project backlog at github more often. We would host more stand up meetings to discuss the progress and make sure that everyone is on the same page. 
