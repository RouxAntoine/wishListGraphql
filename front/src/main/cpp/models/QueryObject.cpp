#ifdef GRAPHQL_DTO_QUERY_HPP__

#include <json/json.h>

namespace GraphQL::Dto
{
    void QueryObject::Serialize( ::Json::Value& root )
    {
        // serialize primitives
        root["operationName"] = operationName;
        root["query"] = query;
        root["variables"] = variables;
    }
    
    void QueryObject::Deserialize( ::Json::Value& root )
    {
        // deserialize primitives
        std::string operationName = root.get("operationName", "").asString();
        std::string query = root.get("query", "").asString();
        std::string variables = root.get("variables", "").asString();

        // m_nTestInt = root.get("testintA",0).asInt();
        // m_fTestFloat = root.get("testfloatA", 0.0).asDouble();
        // m_TestString = root.get("teststringA", "").asString();
        // m_bTestBool = root.get("testboolA", false).asBool();
    }

}

#endif  // GRAPHQL_DTO_QUERY_HPP__
