package com.dico.modules.mapper;

import com.dico.modules.po.CostApply;

public interface CostApplyMapper {
	void save(CostApply apply);

	CostApply getCostApply(int id);

	int updateByPrimaryKey(CostApply record);
}
