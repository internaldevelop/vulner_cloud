package com.vulner.unify_auth.dao;

import com.vulner.unify_auth.bean.po.LicenseDataPo;
import com.vulner.unify_auth.bean.po.LicenseExpirePo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface LicenseDao {

    @Insert("INSERT INTO license_data (\n" +
            "\t`uuid`,\n" +
            "\t`issuer_uuid`,\n" +
            "\t`issuer_name`,\n" +
            "\t`license_data`,\n" +
            "\t`create_time`\n" +
            ")\n" +
            "VALUES(\n" +
            "\t#{uuid},\n" +
            "\t#{issuer_uuid},\n" +
            "\t#{issuer_name},\n" +
            "\t#{license_data},\n" +
            "\t#{create_time}\n" +
            ")")
    int createLicense(LicenseDataPo licenseDataPo);

    @Update("UPDATE license_data SET " +
            "uuid=#{uuid}," +
            "issuer_uuid=#{issuer_uuid}," +
            "issuer_name=#{issuer_name}," +
            "use_flag=#{use_flag}," +
            "use_time=#{use_time} " +
            " WHERE uuid =#{uuid}")
    int updLicense(LicenseDataPo licenseDataPo);

    @Select("<script>" +
                "SELECT\n" +
                "	id,\n" +
                "	uuid,\n" +
                "	issuer_uuid,\n" +
                "	issuer_name,\n" +
                "	license_data,\n" +
                "	use_flag,\n" +
                "	create_time \n" +
                "FROM\n" +
                "	license_data \n" +
                "WHERE\n" +
                "	uuid = #{uuid}\n" +
                "<when test = \"use_flag != null\">\n" +
                "   AND use_flag = #{use_flag}\n" +
                "</when>\n" +
            "</script>")
    LicenseDataPo getInfoByUuid(@Param("uuid") String licenseUuid, @Param("use_flag") int useFlag);

    @Insert("INSERT INTO license_expire (" +
                "uuid, " +
                "issuer_uuid, " +
                "issuer_name, " +
                "user_uuid, " +
                "user_name, " +
                "expire_time, " +
                "create_time" +
            ")\n" +
            "VALUES (" +
                "#{uuid}, " +
                "#{issuer_uuid}, " +
                "#{issuer_name}, " +
                "#{user_uuid}, " +
                "#{user_name}, " +
                "#{expire_time}, " +
                "#{create_time}" +
            ")")
    int addLicenseExpire(LicenseExpirePo licenseExpirePo);


}
