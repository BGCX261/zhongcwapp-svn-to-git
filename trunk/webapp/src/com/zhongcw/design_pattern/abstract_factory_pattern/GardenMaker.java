package com.zhongcw.design_pattern.abstract_factory_pattern;

class GardenMaker {
	// Abstract Factory which returns one of three gardens
	private Garden gd;

	public Garden getGarden(String gtype) {
		gd = new VegieGarden(); // default
		if (gtype.equals("Perennial")) {
			// gd = new PerennialGarden();
		}
		if (gtype.equals("Annual")) {
			// gd = new AnnualGarden();
		}
		return gd;
	}
}
