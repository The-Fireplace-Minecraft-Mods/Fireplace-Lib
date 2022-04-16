package dev.the_fireplace.lib.api.client.interfaces;

import io.netty.util.concurrent.Promise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface CustomButtonScreen<T>
{
    Promise<Optional<T>> getNewValuePromise();
}
