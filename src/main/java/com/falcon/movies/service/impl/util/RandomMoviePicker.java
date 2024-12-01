package com.falcon.movies.service.impl.util;

import com.falcon.movies.service.projection.MovieToWatchProjection;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The class provides methods for generating a collection from a movies list.<br/>
 * The provided movies list is copied to a new list because the elements are shuffled
 * to always generate a list of movies in random order.
 * <br/><br/>
 * The class provides methods for handling both empty and non-empty lists in a chain of invocations. For example: <br/><br/>
 * <code>
 * RandomMoviePicket.from(List.of()).ifEmpty(() -> throw new RuntimeException("Error")).ifNonEmptyCapture(list ->
 * Collections.shuffle(list));
 * </code>
 */
public final class RandomMoviePicker {

    private final List<MovieToWatchProjection> moviesToWatch;

    private RandomMoviePicker(List<MovieToWatchProjection> moviesToWatch) {
        if (moviesToWatch != null) {
            this.moviesToWatch = new ArrayList<>(moviesToWatch);
        } else {
            this.moviesToWatch = List.of();
        }

    }

    public static RandomMoviePicker from(List<MovieToWatchProjection> moviesToWatch) {
        return new RandomMoviePicker(moviesToWatch);
    }

    /**
     * The method captures the variable (<code>moviesToWatch</code>) from the local class to return the first three randomized
     * elements referred to as "movies to watch".
     * The captured variable must be <code>final</code>, and if is not, the compiler will attempt to treat it as effective final.
     * @return Three randomized movies to watch.
     */
    public Supplier<List<MovieToWatchProjection>> captureMovies() {
        Collections.shuffle(moviesToWatch);
        return () -> moviesToWatch.stream().limit(3).toList();
    }

    /**
     * The method will execute the action referenced by the <code>Runnable</code> interface <b>only</b> if the
     * moviesToWatch list is empty.
     * @param action the action to be performed if the <code>moviesToWatch</code> list is empty.
     * @return The instance of the currently used class, used to perform other chained method.
     */
    public RandomMoviePicker ifEmpty(Runnable action){
        if (moviesToWatch.isEmpty()) {
            action.run();
        }
        return this;
    }

    /**
     * The method is invoked regardless of whether the <code>moviesToWatch</code> list is empty or not.
     * It should be called as the last method in the chain of method invocations.
     * <br/>
     * If the <code>moviesToWatch</code> list is not empty, the method will invoke an operation from the <code>captureMovies</code> method
     * and return a randomized list of movies to watch. Any changes made to the list within the <code>Consumer</code> action
     * will not affect the returned result, thanks to the creation of a new list.
     * <br/>
     * If the <code>moviesToWatch</code> list is empty, the method will return empty unmodifiable list.
     * @param action The <code>Consumer</code> action to be performed when the list is not empty.
     * @return List of the MovieToWatchProjection.
     */
    public List<MovieToWatchProjection> ifNonEmptyCapture(Consumer<List<MovieToWatchProjection>> action){
        if (!moviesToWatch.isEmpty()) {
            List<MovieToWatchProjection> capturedRandomMovies = captureMovies().get();
            action.accept(new ArrayList<>(capturedRandomMovies));
            return capturedRandomMovies;
        }
        return moviesToWatch;
    }


}
