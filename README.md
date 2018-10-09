# Guestlogix Take Home Test - Mobile

Created by: Danny Loo
Email: dloo.uwo@gmail.com

********************V1.1********************
********************Sept 16, 2018********************

Requirements can be found here: https://github.com/Guestlogix/mobile-take-home

- User enters the beginning and ending IATA airport locations (airport and airline data can be found in "/data" folder).
- The blue marker on the map indicates the start location.  
- If the user enters an airport code that is not valid, they will be notified.
- If the user enters an incorrect destination, they will be notified.
- Cannot have a blank start/destination
- Routes that are not direct flights and that are servicable through other airports will be displayed


		ie: 	YVR > OTP = YVR > FRA > IST > OTP 	
	
			YYZ > ONT = YYZ > DEN > ONT	
		
			YYZ > KKW = Route does not exist	
		
The routes are found using breadth width search and optimized for the minimum amount of connecting flights. Also the connecting flight are optimized alphabetically, which is why the YVR > OTP flight is serviced through FRA airport instead of the MUC airport as 'F' comes before 'M'. This optimization results in a faster search			
	
- Direct flights will be displayed if there exists a direct flight path
- User can click markers to see additional information(if its a connecting flight, connecting flight #, or destination airport)
- If the route does not exists, the user will be notified 
- Only valid routes are mapped 
- Cannot have a blank start/destination

The release APK can be also tested. It is found in app/app-release.apk
