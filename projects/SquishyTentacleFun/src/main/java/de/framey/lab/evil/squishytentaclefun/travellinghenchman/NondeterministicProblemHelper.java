package de.framey.lab.evil.squishytentaclefun.travellinghenchman;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class NondeterministicProblemHelper<T>  {

    private Stack<List<T>> backlog = new Stack<>();
    
    private T doneToken;
    
    private Predicate<List<T>>      isTokenPathComplete;
    private BiPredicate<List<T>, T> isNextTokenValid;
    
    public NondeterministicProblemHelper(T doneToken,Predicate<List<T>> isTokenPathComplete, BiPredicate<List<T>, T> isNextTokenValid) {
        this.doneToken = doneToken;
        this.isNextTokenValid = isNextTokenValid;
        this.isTokenPathComplete = isTokenPathComplete;
    }
    
    public T oneOf(@SuppressWarnings("unchecked") T... tokens) {
        if (backlog.isEmpty()) {
            backlog.push(new ArrayList<>());
        }
        List<T> currentTokenPath = backlog.pop();
        for (T token : tokens) {
            if (isNextTokenValid.test(currentTokenPath, token)) {
                List<T> nextTokenPath = new ArrayList<>(currentTokenPath);
                nextTokenPath.add(token);
                backlog.push(nextTokenPath);
            }
        }
        if (backlog.isEmpty() || backlog.peek().isEmpty()) {
            return doneToken;
        }
        if (isTokenPathComplete.test(backlog.peek())) {
            backlog.pop();
        }
        if (backlog.isEmpty() || backlog.peek().isEmpty()) {
            return doneToken;
        }
        return backlog.peek().get(backlog.peek().size() - 1);
    }
}
