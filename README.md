# Boardwalk
This was developed as the second group project at Academia de Codigo's Bootcamp#29, at the end of the 7th week. We built it in three days.

In this game you are a pirate and you want to find the treasure's key. How can you do that? Unravel the expression, letter by letter, or try to guess the entire expression. For every letter you miss, your ship sinks a bit.

It allows multiplayer via Netcat with players being able to create separate game rooms and password protect them, allowing multiple instances being run simultaneously.

## Deployment
We've provided the build.xml file so you can build the .Jar with "ant" on terminal.

## Usage
Run the program and connect the clients via Netcat to the IP of the machine where the .Jar is being run on, port 2929: "nc x.x.x.x 2929".

## Features

Players choose an alias  
Create a room to play or join one already created  
Protect the room with password (optional)  
Chat in the room before game starts  
Can't choose an already chosen letter  
See the game instructions

## Screenshots

![Initial Screen](https://i.imgur.com/ceOizPv.png)

![Room Selection](https://i.imgur.com/nE6VqDg.png)

![Gameplay](https://i.imgur.com/aj1mb2s.png)

![Midgame](https://i.imgur.com/FGntOPr.png)

![Endgame](https://i.imgur.com/XJyBwmh.png)
