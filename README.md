# Guestlogix Take Home Test - Mobile

Created by: Danny Loo
Email: dloo.uwo@gmail.com


********************Old Submission********************
********************V1.0********************
********************Sept 10, 2018********************
How the app works:

- User enters the beginning and ending IATA locations 
- The user can add as many locations as they want by pressing the "+" button, and scrolling through the list
- Likewise a location can be removed by pressing the "-" button
- The resulting route will be a line drawn from start -> destination 1 -> destination 2 -> etc to simulate
the route of a plane. 
- The blue marker on the map indicates the start location.  
- The user can click on any marker to see more information on the trip 
- If the user enters an incorrect start, they will be told the start is not valid 
- If the user enters an incorrect destination, they will be prompt on which destination is incorrect
- Only valid routes are mapped 
- Cannot have a blank start/destination

The release APK can be also tested. It is found in app/app-release.apk




********************New Submission********************
********************V1.1********************
********************Sept 16, 2018********************

Changes made are as follows: 

- Routes that are not direct flights and that are servicable through other airports will be displayed
	ie: 	YVR > OTP = YVR > FRA > IST > OTP 
		YYZ > ONT = YYZ > DEN > ONT
		YYZ > KKW = Route does not exist
		
The routes are found using breadth width search and optimized for the minimum amount of connecting flights. Also the connecting flight are optimized alphabetically, which is why the YVR > OTP flight is serviced through FRA airport instead of the MUC airport as 'F' comes before 'M'. This optimization results in a faster search
			
	
- Direct flights will be displayed if there exists a direct flight path
- Beginning airport is a blue marker
- User can click markers to see additional information(if its a connecting flight, connecting flight #, or destination airport)
- If the route does not exists, the user will be notified 
- If the user enters an incorrect start, they will be told the start is not valid 
- If the user enters an incorrect destination, they will be told the destination is incorrect
- Only valid routes are mapped 
- Cannot have a blank start/destination

The release APK can be also tested. It is found in app/app-release.apk
