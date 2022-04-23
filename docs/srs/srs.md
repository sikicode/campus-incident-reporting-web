Security App SRS Document

**Problem Statement:** A few sentences to describe the problem you are trying to solve, i.e., justify why this software is needed.

Public safety has always been a concern for JHU faculty and students, especially who live in bad neighborhoods. The problem is that the current email alert system isn’t hooked up with users well, alerts either be made too late or left unreported. Users need an accurate and precise way to score and report incidents they meet, and view other incidents occur near them.

**Potential Clients:** Who are influenced by this problem and would benefit from the proposed solution? (i.e. the potential users)

JHU faculty and students would benefit the most, other residents who are outdoor active (dog walk, commute, exercise) will also be potential users.

**Proposed Solution:** Write a few sentences that describe how a software solution will solve the problem described above.

A security alert web app with functions to view, score, and report incidents in near neighborhoods. The app will also have social functions for users to discuss incidents and watch over each other.

**Functional Requirements:** List the (functional) requirements that software needs to have in order to solve the problem stated above. It is useful to try to group the requirements into those that are essential (must have), and those which are non-essential (but nice to have).

**Must have:**
  
As a user, I want to:  
 - Interact with a map interface so that I am familiar with the environment in the neighborhood.
 - Report incidents so that I can share the information with the community.
 - View all incidents so that I have a general idea of what has happened before.
 - View details of each incident so that I learned more about what has happened before.

As a campus security team member, I want to:
 - Allow only signed-in users to report incidents so that I can ensure the security and genuineness of our user community.

**Nice to have:**

As a user, I want to:
  - See the total number of incidents over an area on map so that I know if a neighbourhood is danganerous.
  - View contact information of the campus security so that I know how to contact them.
  - View incidents based on their dates so that I can learn about more recent incidents.

As a campus security team member, I want to:
 - View incidents based on their dates and types so that I can learn what needs to be improved.
 - Receive an email noification when an incident is reported so I can reach out and take actions.

**Software Architecture:**
1.	As an incident report system, I want an incident scale system to score incidents to different severity so that low-risk and high-risk incidents can be differentiated. (3rd party crime report can be used)
2.	As a social component of the web app, I want an authentication system so that only authorized users can interact with posts and comments.
3.	As a front end, I want a map API so that I can visualize data on a map.
