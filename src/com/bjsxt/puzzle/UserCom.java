package com.bjsxt.puzzle;

import java.io.Serializable;
import java.util.Comparator;

public class UserCom implements Comparator<User>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//�ȽϹ����ǣ��ȱȽ��ƶ�������������ͬ���Ƚ���Ϸ���𣬼�����ͬ�Ƚ���Ϸ��ʱ
	@Override
	public int compare(User o1, User o2) {
		if(o1.getMoveCount()!=o2.getMoveCount()){
			return o1.getMoveCount()-o2.getMoveCount();
		}else{
			if(o2.getGameLevel()!=o1.getGameLevel()){
				return o2.getGameLevel()-o1.getGameLevel();
			}else{
				return (int)(o1.getGameTime()-o2.getGameTime());
			}
		}
		
	}
	
}
