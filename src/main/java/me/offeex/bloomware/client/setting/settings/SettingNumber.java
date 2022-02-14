package me.offeex.bloomware.client.setting.settings;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.Setting;
import me.offeex.bloomware.client.setting.SettingValueUpdateBus;

public class SettingNumber extends Setting {
    public final SettingValueUpdateBus<Double> valueUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());
    public final SettingValueUpdateBus<Double> minUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());
    public final SettingValueUpdateBus<Double> maxUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());
    public final SettingValueUpdateBus<Double> incUpdateBus = this.registerUpdateBus(new SettingValueUpdateBus<>());

    private double value;
    private final double defaultValue;
    private double min, max, inc;

    public double getDefaultValue() {
        return defaultValue;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getInc() {
        return inc;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value < min || value > max) throw new IllegalArgumentException();
        double oldValue = this.value;
        this.value = ((value * 1000) - ((value * 1000) % (inc * 1000))) * 0.001;
        this.valueUpdateBus.trigger(oldValue, this.value);
    }

    public void setMin(double min) {
        double oldValue = this.min;
        this.min = min;
        this.minUpdateBus.trigger(oldValue, this.min);
    }

    public void setMax(double max) {
        double oldValue = this.max;
        this.max = max;
        this.maxUpdateBus.trigger(oldValue, this.max);
    }

    public void setInc(double inc) {
        double oldValue = this.inc;
        this.inc = inc;
        this.incUpdateBus.trigger(oldValue, this.inc);
    }

    private SettingNumber(String name, Module module, String description, double value, double min, double max, double inc) {
        super(name, module, description);
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.defaultValue = value;
    }

    public static class Builder extends Setting.Builder {
        private double value = 0.0;
        private double min = Double.MIN_VALUE;
        private double max = Double.MAX_VALUE;
        private double inc = 1.0;

        public Builder(String name, String description) {
            super(name, description);
        }

        public Builder(String name) {
            super(name, "");
        }

        public Builder value(double value) {
            this.value = value;
            return this;
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder inc(double inc) {
            this.inc = inc;
            return this;
        }

        @Override
        protected Setting make(Module module) {
            return new SettingNumber(name, module, description, value, min, max, inc);
        }

        @Override
        public SettingNumber setup(Module module) {
            SettingNumber s = new SettingNumber(name, module, description, value, min, max, inc);
            Bloomware.settingManager.add(s);
            return s;
        }

        @Override
        public SettingNumber setup(SettingGroup sg) {
            SettingNumber s = new SettingNumber(name, sg.getModule(), description, value, min, max, inc);
            sg.getSettings().add(s);
            Bloomware.settingManager.add(s);
            return s;
        }
    }
}