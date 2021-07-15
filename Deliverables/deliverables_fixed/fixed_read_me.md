Bugs fixed:
1. During last tests, we noticed that there was a NullPointerException after a player disconnection before the start of the match
in the waiting room due to a functionality that we implemented past days and we haven't tested that behaviour.
Fortunatly we solved added one line of code in MainServer:196 adding 
	if(discPlayer !=null)

2. We also fixed the other bug about reconnection problem indicated in original readme file.
We solved that issue introducing a setter method of newGame in Server:335 and using that setter in Server:52. 

Modified jars have been added in this directory and the way to start them is the same as the old way in README.md 