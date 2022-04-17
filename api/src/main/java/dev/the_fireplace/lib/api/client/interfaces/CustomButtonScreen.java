package dev.the_fireplace.lib.api.client.interfaces;

import io.netty.util.concurrent.Promise;

import java.util.Optional;

/**
 * Client side only
 */
public interface CustomButtonScreen<T>
{
    Promise<Optional<T>> getNewValuePromise();
}
