package cn.leo.dcinema.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum SharpnessEnum {
	SD("流畅", 0), BIAO("标清", 1), HD("高清", 2), SUPER("超清", 3), BLUE("蓝光", 4), _3D(
			"3D", 5);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private SharpnessEnum(String name, int index) {
		this.name = name;
		this.index = index;
	}

	// 普通方法
	public static String getName(int index) {
		for (SharpnessEnum s : SharpnessEnum.values()) {
			if (s.getIndex() == index) {
				return s.getName();
			}
		}
		return null;
	}

	// get set 方法
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public static SharpnessEnum getSharp(int parseInt) {
		// TODO Auto-generated method stub
		SharpnessEnum sharpnessenum = null;
		switch (parseInt) {
		case 0:
			sharpnessenum = SD;
			break;
		case 1:
			sharpnessenum = BIAO;
			break;
		case 2:
			sharpnessenum = HD;
			break;
		case 3:
			sharpnessenum = SUPER;
			break;
		case 4:
			sharpnessenum = BLUE;
			break;
		case 5:
			sharpnessenum = _3D;
			break;

		default:
			break;
		}
		return sharpnessenum;
	}

	@SuppressWarnings("unchecked")
	public static SharpnessEnum getSuitSharp(SharpnessEnum sharpnessenum,
			List<SharpnessEnum> list) {

		if (!list.contains(sharpnessenum)) {
			ArrayList<Integer> arraylist = new ArrayList<Integer>();
			for (int i = 0; i < list.size(); i++) {
				int j = Math.abs(((SharpnessEnum) list.get(i)).index
						- sharpnessenum.index);
				System.out.println(j);
				arraylist.add(Integer.valueOf(j));
			}
			ArrayList<Integer> arraylist1 =  (ArrayList<Integer>) arraylist.clone();
			Collections.sort(arraylist1);
			sharpnessenum = (SharpnessEnum) list.get(arraylist.indexOf(Integer
					.valueOf(((Integer) arraylist1.get(0)).intValue())));
		}
		return sharpnessenum;
	}

}
