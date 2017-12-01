% Author:
% Date: 11/11/2017

row(3).
column(3).
teleportal(2,2).
obstacle(2,0).
pressure_pad(0,2).
pressure_pad(0,0).

%the boundaries of the wall
wall(X,Y):-
           row(M),
           column(N),
           X<M,
           Y<N,
           X>=0,
           Y>=0.

% agent axiom
agent(2,1,s0). %initial agent position
agent(X, Y, result(forward,S)):- % agent moves forward in an empty cell
                                wall(X,Y),
                                \+rock(X,Y,S),
                                \+obstacle(X,Y),
                                XP1 is X+1,
                                wall(XP1,Y),
                                agent(XP1,Y,S).

agent(X, Y, result(forward,S)):-
% agent moves forward and finds a rock, then the rock will move only if
% its forward isn't an obstacle or a rock
                                rock(X,Y,S),
                                X1 is X - 1,
                                \+obstacle(X1,Y),
                                \+rock(X1,Y,S),
                                XP1 is X+1,
                                wall(XP1,Y),
                                agent(XP1,Y,S),
                                wall(X1,Y),
                                wall(X,Y).

agent(X, Y, result(backward,S)):- % agent moves backward in an empty cell
                                wall(X,Y),
                                \+rock(X,Y,S),
                                \+obstacle(X,Y),
                                XP1 is X - 1,
                                wall(XP1,Y),
                                agent(XP1,Y,S).

agent(X, Y, result(backward,S)):-
% agent moves backward and finds a rock, then the rock will move only if
% its backward isn't an obstacle or a rock
                                rock(X,Y,S),
                                X1 is X + 1,
                                \+obstacle(X1,Y),
                                \+rock(X1,Y,S),
                                XP1 is X - 1,
                                wall(XP1,Y),
                                agent(XP1,Y,S),
                                wall(X1,Y),
                                wall(X,Y).

agent(X, Y, result(left,S)):- % agent moves left in an empty cell
                                wall(X,Y),
                                \+rock(X,Y,S),
                                \+obstacle(X,Y),
                                YP1 is Y + 1,
                                wall(X,YP1),
                                agent(X,YP1,S).

agent(X, Y, result(left,S)):-
% agent moves left and finds a rock, then the rock will move only if
% its left isn't an obstacle or a rock
                                rock(X,Y,S),
                                Y1 is Y - 1,
                                \+obstacle(X,Y1),
                                \+rock(X,Y1,S),
                                YP1 is Y + 1,
                                wall(X,Y1),
                                agent(X,YP1,S),
                                wall(X,YP1),
                                wall(X,Y).

agent(X, Y, result(right,S)):- % agent moves right in an empty cell
                                wall(X,Y),
                                \+rock(X,Y,S),
                                \+obstacle(X,Y),
                                YP1 is Y - 1,
                                wall(X,YP1),
                                agent(X,YP1,S).

agent(X, Y, result(right,S)):-
% agent moves right and finds a rock, then the rock will move only if
% its right isn't an obstacle or a rock
                                rock(X,Y,S),
                                Y1 is Y + 1,
                                \+obstacle(X,Y1),
                                \+rock(X,Y1,S),
                                YP1 is Y - 1,
                                wall(X,Y1),
                                agent(X,YP1,S),
                                wall(X,YP1),
                                wall(X,Y).


%rock axioms
%Initial rock positions
rock(1,1,s0).
rock(X,Y,result(forward,S)):- % agent pushed rock forward
                            push(X,Y,2,0,S),
                            X1 is X + 1,
                            rock(X1,Y,S).

rock(X,Y,result(backward,S)):- % agent pushed rock backward
                            push(X,Y,-2,0,S),
                            X1 is X - 1,
                            rock(X1,Y,S).

rock(X,Y,result(left,S)):- % agent pushes rock left
                            push(X,Y,0,2,S),
                            Y1 is Y + 1,
                            rock(X,Y1,S).

rock(X,Y,result(right,S)):- % agent pushes rock right
                            push(X,Y,0,-2,S),
                            Y1 is Y - 1,
                            rock(X,Y1,S).

% Presistance of Rock, agent moved away from the rock and didn't
% push it(moving the rock failed)
rock(X,Y,result(forward,S)):-
                            \+push(X,Y,2,0,S),
                            rock(X,Y,S).

rock(X,Y,result(backward,S)):-
                            \+push(X,Y,-2,0,S),
                            rock(X,Y,S).

rock(X,Y,result(left,S)):-
                            \+push(X,Y,0,-2,S),
                            rock(X,Y,S).

rock(X,Y,result(right,S)):-
                            \+push(X,Y,0,2,S),
                            rock(X,Y,S).

% agent pushes rock
push(X,Y,I,J,S):-
                            wall(X,Y),
                            \+rock(X,Y,S),
                            \+obstacle(X,Y),
                            X2 is X + I,
                            Y2 is Y + J,
                            agent(X2,Y2,S).

% Iterative deepening that calls call_with_depth_limit(Query, C, R)
% Query is the agent and rock queries. Note: we query by the position of
% pressure_pad and teleportal positions.
% C is the depth limit R is the
% depth that the answer was found at
iterative(C,S):-
               call_with_depth_limit((agent(2,2,S),rock(0,1,S)),C,R),
               \+R = depth_limit_exceeded;

               call_with_depth_limit((agent(2,2,S),rock(0,1,S)),C,R),
               R = depth_limit_exceeded,
               C1 is C + 1,
               iterative(C1,S).

