package cc.gooa.facecard.mapper;

import cc.gooa.facecard.model.BusCreditLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BusCreditLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusCreditLog record);

    int insertSelective(BusCreditLog record);

    BusCreditLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BusCreditLog record);

    int updateByPrimaryKeyWithBLOBs(BusCreditLog record);

    int updateByPrimaryKey(BusCreditLog record);
}