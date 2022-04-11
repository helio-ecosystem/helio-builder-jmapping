package helio.jmapping.functions.helio;

import helio.blueprints.TranslationUnit;
import helio.blueprints.UnitType;

class HelioUnitsManager {

	private HelioUnitsManager() {
		super();
	}
	public static boolean isAsync(TranslationUnit unit) {
		return UnitType.Asyc.equals(unit.getUnitType());
	}
	
	public static boolean isSync(TranslationUnit unit) {
		return UnitType.Sync.equals(unit.getUnitType());
	}
	
	public static boolean isScheduled(TranslationUnit unit) {
		return UnitType.Scheduled.equals(unit.getUnitType());
	}
}