It-1 User Story Document

**Functional Requirements:** List the (functional) requirements that software needs to have in order to solve the problem stated above. It is useful to try to group the requirements into those that are essential (must have), and those which are non-essential (but nice to have).

**Must have:**
  
As a user, I want to:  
- view a map interface so that I am familiar with the environment in the neighborhood. (It-1)
- post an incident to database so that an incident record is made.
- mark a posted incident on the map as a pin so that users know there's an incident.

**Nice to have:**

As a user, I want to:
N/A

**Software Architecture:**
1. As a frontend, I want a map API so that I can visualize data on a map.
2. As a backend, I want 
   - a model package so that I can construct and process json data later.
   - a server so that I can host web pages.
3. As a database management system, I want:
   - a local database to store all posted incidents so that I can check records later.
