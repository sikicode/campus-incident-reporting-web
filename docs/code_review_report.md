## Code Review report:


1. Changed the way the app reads data from the database and adds markers to the map, the app runs much faster now. (Gary, Karine)
  - improved **Design**
  - **Complexity**: the length of code is reduced and it’s more clear, easier for another programmer to use and change
2. Changed the variable from var to const so that when the markers are added to the map, the info windows are positioned correctly. (Gary)
  - improved **Style** and solved layout issues
3. Cleaned up unnecessary javascript scripts in vm files.dsa. (Gary)
  - improved **Style**
4. Added **Comments** to highlight the functionalities of the code. (Rahul, Alex)
5. Moved signin signout functions from login.vm to index.js. (Gary)
  - improved **Style**, the functions are separated from the layout
  - **Complexity**: easier for another programmer to find the layout/functions
6. Updated the “how to use application in README”. (Rahul, Alex)
  - updated **Documentation**
7. Renamed daily incidents page to matched its functionality (Karine)
  - improved **Naming**
8. Added Incident Manager class to move data analysis to backend and lifted weight off frontend. (Karine)
  - improved **Design**
