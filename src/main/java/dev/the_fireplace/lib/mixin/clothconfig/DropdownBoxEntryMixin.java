package dev.the_fireplace.lib.mixin.clothconfig;

import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;
import java.util.function.Function;

@Mixin(value = DropdownBoxEntry.DefaultSelectionTopCellElement.class, remap = false)
public abstract class DropdownBoxEntryMixin<R> extends DropdownBoxEntry.SelectionTopCellElement<R>
{
    @Shadow
    protected TextFieldWidget textFieldWidget;
    @Shadow
    protected Function<String, R> toObjectFunction;
    @Shadow
    protected R value;

    @Shadow
    public abstract Optional<String> getError();

    /**
     * @author The_Fireplace
     * @reason Patch the crash when setErrorSupplier is called on a dropdown
     */
    @Overwrite(remap = false)
    public R getValue() {
        return this.getError().isPresent() ? this.value : this.toObjectFunction.apply(this.textFieldWidget.getText());
    }
}
