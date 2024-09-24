package org.example.cstproject;

import java.util.*;
import java.util.stream.Collectors;

class State {
    String name;
    boolean isAccepting;

    State(String name, boolean isAccepting) {
        this.name = name;
        this.isAccepting = isAccepting;
    }
}

class Transition {
    State fromState;
    char inputSymbol;
    State toState;

    Transition(State fromState, char inputSymbol, State toState) {
        this.fromState = fromState;
        this.inputSymbol = inputSymbol;
        this.toState = toState;
    }
}

class DFA {
    private final Set<State> states;
    private final Set<Transition> transitions;
    private State startState;
    private State currentState;  // Single declaration
    private final Map<State, Map<Character, State>> transitionMap;

    public DFA() {
        states = new HashSet<>();
        transitions = new HashSet<>();
        transitionMap = new HashMap<>();
    }

    public void addState(State state) {
        states.add(state);
        transitionMap.put(state, new HashMap<>());
    }

    public void addTransition(Transition transition) {
        transitions.add(transition);
        transitionMap.get(transition.fromState).put(transition.inputSymbol, transition.toState);
    }

    public void setStartState(State state) {
        this.startState = state;
        this.currentState = state;  // Initialize current state to start state
    }

    public boolean accepts(String input) {
        currentState = startState; // Reset current state
        for (char symbol : input.toCharArray()) {
            Map<Character, State> transitions = transitionMap.get(currentState);
            if (transitions != null && transitions.containsKey(symbol)) {
                currentState = transitions.get(symbol);
            } else {
                return false; // No valid transition
            }
        }
        return currentState.isAccepting; // Accept if in an accepting state
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<Transition> getTransitions() {
        return transitions;
    }

    public State getStateByName(String name) {
        for (State state : states) {
            if (state.name.equals(name)) {
                return state;
            }
        }
        return null; // Return null if not found
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
    }

    public boolean isAccepting(State state) {
        return state.isAccepting;
    }

    public Transition getTransition(State currentState, char input) {
        // Logic to find the transition based on the current state and input
        for (Transition transition : transitions) {
            if (transition.fromState.equals(currentState) && transition.inputSymbol == input) {
                return transition;
            }
        }
        return null; // No valid transition
    }

    public State getStartState() {
        return startState;
    }
    public List<Transition> getTransitionsFromState(State state) {
        return transitions.stream()
                .filter(t -> t.fromState.equals(state))
                .collect(Collectors.toList());
    }
}
