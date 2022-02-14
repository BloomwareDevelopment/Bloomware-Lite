package me.offeex.bloomware.client.setting;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.module.Module;
import me.offeex.bloomware.client.setting.settings.SettingGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class Setting {
	private final String name;
	private String id;
	private final String description;
	private final Module module;
	private final List<SettingValueUpdateBus<?>> updateBuses = new ArrayList<>();
	private final SettingUpdateBus updateBus = new SettingUpdateBus();

	private boolean active = true;
	private Setting[] dependencies = new Setting[0];
	private DependResolver activityResolver = () -> true;

	public <T extends Setting> T depend(DependResolver resolver, Setting... dependencies) {
		for (Setting d : this.dependencies)
			for (SettingValueUpdateBus<?> bus : d.updateBuses)
				bus.unSubscribe(this::handleSettingUpdate);
		for (Setting d : dependencies)
			for (SettingValueUpdateBus<?> bus : d.updateBuses)
				bus.subscribe(this::handleSettingUpdate);
		this.dependencies = dependencies;
		this.activityResolver = resolver;
		this.updateActive();
		return (T) this;
	}

	private void handleSettingUpdate(Object a, Object b) {
		this.updateActive();
	}

	private void updateActive() {
		this.active = this.activityResolver.method();
		this.updateBus.trigger();
	}

	protected <T> SettingValueUpdateBus<T> registerUpdateBus(SettingValueUpdateBus<T> bus) {
		this.updateBuses.add(bus);
		return bus;
	}

	protected Setting(String name, Module module, String description) {
		this.name = name;
		this.module = module;
		this.description = description;
	}

	public String getType() {
		return this.getClass().getSimpleName();
	}

	public String getName() {
		return name;
	}

	public Module getModule() {
		return module;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasDescription() {
		return description != null;
	}

	public String getId() {
		return id;
	}

	public boolean isActive() {
		return this.active;
	}

	public SettingUpdateBus getUpdateBus() {
		return updateBus;
	}

	public void setId(String newId) {
		this.id = newId;
	}

	public abstract static class Builder {
		protected final String name;
		protected final String description;

		public Builder(String name, String description) {
			this.name = name;
			this.description = description;
		}

		protected abstract Setting make(Module module);

		public <T extends Setting> T setup(Module m) {
			Setting s = this.make(m);
			Bloomware.settingManager.add(s);
			return (T) s;
		}
		public <T extends Setting> T setup(SettingGroup sg) {
			Setting s = this.make(sg.getModule());
			sg.getSettings().add(s);
			Bloomware.settingManager.add(s);
			return (T) s;
		}
	}

	@FunctionalInterface
	public interface DependResolver {
		boolean method();
	}
}