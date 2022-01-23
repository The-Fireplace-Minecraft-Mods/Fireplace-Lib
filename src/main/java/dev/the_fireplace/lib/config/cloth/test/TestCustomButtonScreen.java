package dev.the_fireplace.lib.config.cloth.test;

import dev.the_fireplace.lib.api.client.interfaces.CustomButtonScreen;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Optional;
import java.util.Random;

@Environment(EnvType.CLIENT)
public final class TestCustomButtonScreen extends Screen implements CustomButtonScreen<String>
{

    private final Promise<Optional<String>> promise;
    private final Screen parent;
    private String newValue;

    public TestCustomButtonScreen(Screen parent, String currentValue) {
        super(Text.of("Button Custom Screen Test. New value:"));
        promise = new DefaultPromise<>(new DefaultEventExecutor());
        this.newValue = currentValue;
        this.parent = parent;
    }

    @Override
    public Promise<Optional<String>> getNewValuePromise() {
        return promise;
    }

    @Override
    protected void init() {
        this.addButton(new ButtonWidget(this.width / 2 - 202, this.height - 60, 200, 20, Text.of("Assign random value"), (button) -> {
            this.newValue = generateValue();
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 202, this.height - 60 + 22, 200, 20, Text.of("Confirm and exit"), (button) -> {
            closeScreen();
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 2, this.height - 60, 200, 20, Text.of("Cancel"), (button) -> {
            promise.setSuccess(Optional.empty());
            closeScreen();
        }));
    }

    private void closeScreen() {
        onClose();
        MinecraftClient.getInstance().openScreen(parent);
    }

    private String generateValue() {
        return String.valueOf(new Random().nextDouble());
    }

    @Override
    public void onClose() {
        if (!promise.isDone()) {
            promise.setSuccess(Optional.of(newValue));
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, this.height / 2 - 9 * 2, 0xFFFFFF);
        drawCenteredText(matrices, this.textRenderer, Text.of(newValue), this.width / 2, this.height / 2, 0xFFFFFF);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
