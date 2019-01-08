#ifdef GRAPHQL_DTO_SERIALIZABLE_HPP__

#include <json/json.h>

namespace GraphQL::Dto
{

    void SerializableObject::Serialize( ::Json::Value& root )
    {
        // serialize primitives
        root["testintA"] = m_nTestInt;
        root["testfloatA"] = m_fTestFloat;
        root["teststringA"] = m_TestString;
        root["testboolA"] = m_bTestBool;
    }
    
    void SerializableObject::Deserialize( ::Json::Value& root )
    {
        // deserialize primitives
        m_nTestInt = root.get("testintA",0).asInt();
        m_fTestFloat = root.get("testfloatA", 0.0).asDouble();
        m_TestString = root.get("teststringA", "").asString();
        m_bTestBool = root.get("testboolA", false).asBool();
    }

}

#endif  // GRAPHQL_DTO_SERIALIZABLE_HPP__
