Introduction
Welcome to Get Stuck!, a card game developed in Java. This README provides an overview of the game, how to get started, and how to play. Feel free to explore the code and customize the game as you like.

Game Description
Get Stuck! is a two-player card game where the objective is to outscore your opponent. The game board consists of a grid of cards, each with a value. Players take turns moving cards to maximize their scores while following certain rules. The game continues until neither player can make a valid move.

Prerequisites
Before playing the game, you'll need the following:
Java Development Kit (JDK) installed on your computer.

Installation
In order to compile and run the game, first extract the zip file into a desired folder on
your device, secondly download Eclipse Java IDE and open up the program. Then in
Eclipse go to “File” > “Import” > “Existing projects into workspace” and then click on
the next button, after that, select the folder you extracted the zip folder into and click
on the “GetStuck” project, then click finish. Then on the left, click on then
“GetStuck.java” file and click on the run button (a green play symbol) on the top of
the U.I in eclipse, then the Get Stuck game should run successfully, and you can
choose which parameters to test and run the program with.

How to Play
Objective: The goal is to have the highest score when the game ends.
Turns: Players take turns making moves on the game board.
Valid Moves: You can move cards vertically, horizontally, or diagonally. However, there are rules regarding how cards can be moved:
A card with a face value less than 10 can move over other cards.
A card with a face value of 10 or more cannot move over other cards.
Game End: The game continues until neither player can make a valid move. The player with the highest score wins.

Features
Two-player card game.
Customizable settings, including AI vs. AI mode and alpha-beta pruning.
Score tracking and game statistics.
User-friendly graphical interface.
Restart option during gameplay.

Settings
The game includes a settings window where you can configure the following options:
Game Mode: Choose between "Two Player" or "Single Player" mode.
AI vs. AI: Enable or disable AI vs. AI mode.
Pruning: Enable or disable alpha-beta pruning.
Total Games: Specify the total number of games in AI vs. AI mode.
Max Look Ahead: Set the maximum look-ahead for the AI (in AI vs. AI mode).

Development
The game is written in Java and uses Swing for the graphical interface.

License
This project is licensed under the MIT License.
