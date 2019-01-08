#ifdef GRAPHQL_HTTP_HPP__

#include <iostream>
#include <unordered_map>
#include <string>
#include <algorithm>
#include <type_traits>

#include "../hpp/IJsonSerializable.hpp"
#include "../hpp/GraphQLHTTP.hpp"
#include "../hpp/Utils.hpp"

namespace GraphQL::Http
{
    // http post request with object class
    template<typename CLAZZ, typename std::enable_if<std::is_base_of<GraphQL::Json::IJsonSerializable, CLAZZ>::value>::type* = nullptr>
    std::string HttpClient::post(
        std::string url, const CLAZZ obj, 
        const std::unordered_map<std::string, std::string>& headers
    )
    {
        std::string buffer = "";
        std::string serializedObject = "";
        CLAZZ nonConstObj = obj;
        
        bool success = GraphQL::Json::CJsonSerializer::Serialize( &nonConstObj, serializedObject);
        if(success) {
            curl_slist* list = NULL;
            
            this->init();
            this->setHttps(url);
            this->setOpt(url, buffer);
            this->setBodyContent(&serializedObject, list);
            this->setHeaders(headers, list);
            bool ok = this->exec(buffer);
            if(ok) {
                if(this->debug) {
                    InfoStruct info = this->getInfo("POST");
                    printf("[%s] to '%s' in [%.1f sec] give \"%ld\" %" CURL_FORMAT_CURL_OFF_T " byte\n", 
                        info.verb.c_str(), info.host.c_str(), info.time,
                        info.status, info.size);
                }
            }
            this->clean(list);
        }
        else {
            std::cerr << "error during class serialization" << std::endl;
        }

        return buffer;
    }

    // http get request
    std::string HttpClient::get(std::string url, 
        const std::unordered_map<std::string, std::string>& queryParams,
        const std::unordered_map<std::string, std::string>& headers
    ) {        
        std::string buffer;
        curl_slist* list = NULL;
        
        this->init();
        std::string urlWithParam = this->setQueryParam(url, queryParams);
        this->setHttps(url);
        this->setOpt(urlWithParam, buffer);
        this->setHeaders(headers, list);
        bool ok = this->exec(buffer);
        if(ok) {
            if(this->debug) {
                InfoStruct info = this->getInfo("GET");
                printf("[%s] to '%s' in [%.1f sec] give \"%ld\" %" CURL_FORMAT_CURL_OFF_T " byte\n", 
                    info.verb.c_str(), info.host.c_str(), info.time,
                    info.status, info.size);
            }
        }
        this->clean(list);

        return buffer;
    }

    // init curl library
    bool HttpClient::init() 
    {
        curl_global_init(CURL_GLOBAL_DEFAULT);
    
        this->curlObj = curl_easy_init();
        if(this->curlObj == NULL) {
            this->handleError(true, "Failed to create CURL connection");
            return false;
        }
        return true;
    }

    // complete a given baseUrl with query param
    std::string HttpClient::setQueryParam(std::string url, const std::unordered_map<std::string, std::string>& queryParams)
    {

        auto it = queryParams.begin();
        std::string urlWithParam = url + "?" + it->first + "=" + this->urlEncode(it->second);
        
        std::for_each(++it, queryParams.end(), 
            [&](const std::unordered_map<std::string, std::string>::value_type& pair) { 
                urlWithParam += "&"; 
                urlWithParam += pair.first;
                urlWithParam += "="; 
                urlWithParam += this->urlEncode(pair.second);
            }
        );
        return urlWithParam;
    }

    // set https mode if url contains https
    void HttpClient::setHttps(std::string& url) 
    {
    	if(url.find("https") == std::string::npos) {
    		// define option related to SSL
    		curl_easy_setopt(this->curlObj, CURLOPT_SSL_VERIFYPEER, 0L);
    		curl_easy_setopt(this->curlObj, CURLOPT_SSL_VERIFYHOST, 0L);
    	}
    }

    // encode string for pass it to url
    std::string HttpClient::urlEncode(std::string toEncode) 
    {
        std::string res = "";

    	char* output = curl_easy_escape(this->curlObj, toEncode.c_str(), 0);
    	if(!output) {
            this->handleError(true, "Failed to encode url %s", toEncode);
    	}
    	else {
    		res = std::string(output);
    		curl_free(output);
    	}
        return res;
    }

    // set post param on query
    void HttpClient::setBodyContent(std::string* body, curl_slist* list)
    {
    	CURLcode codeResult;

        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_POST, 1L);
        this->handleError(codeResult != CURLE_OK, "Failed to set POST verb [%d]", codeResult);

        // set post data reader fonction
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_READFUNCTION, GraphQL::Utils::reader);
        this->handleError(codeResult != CURLE_OK, "Failed to set writer [%s]", this->errorBuffer);

        // set post data
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_READDATA, body);
        this->handleError(codeResult != CURLE_OK, "Failed to set POST data (data : %s) [%d]", *body, codeResult);

        // set post data length
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_POSTFIELDSIZE_LARGE, body->size());
        this->handleError(codeResult != CURLE_OK, "Failed to set POST data FIELDSIZE (size : %d) [%d]", body->size(), codeResult);

        // codeResult = curl_easy_setopt(this->curlObj, CURLOPT_POSTFIELDS, body->c_str());
        // this->handleError(codeResult != CURLE_OK, "Failed to set POST data (data : %s) [%d]", *body, codeResult);
    }

    // set curl option
    void HttpClient::setOpt(std::string url, std::string& buffer) 
    {
    	CURLcode codeResult;

        // set verbose option in debug mode
        if(this->debug) {
            curl_easy_setopt(this->curlObj, CURLOPT_VERBOSE, 1L);
        }

        // set buffer for error nessage
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_ERRORBUFFER, this->errorBuffer);
        this->handleError(codeResult != CURLE_OK, "Failed to set error buffer [%d]", codeResult);

        // set query url
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_URL, url.c_str());
        this->handleError(codeResult != CURLE_OK, "Failed to set URL [%s]", this->errorBuffer);

        // set follow location parameter
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_FOLLOWLOCATION, 1L);
        this->handleError(codeResult != CURLE_OK, "Failed to set redirect option [%s]", this->errorBuffer);

        // set writer fonction
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_WRITEFUNCTION, GraphQL::Utils::writer);
        this->handleError(codeResult != CURLE_OK, "Failed to set writer [%s]", this->errorBuffer);

        // set buffer for success
        codeResult = curl_easy_setopt(this->curlObj, CURLOPT_WRITEDATA, &buffer);
        this->handleError(codeResult != CURLE_OK, "Failed to set write data [%s]", this->errorBuffer);
    }

    // get header map : format and add it to CURL query context
    void HttpClient::setHeaders(const std::unordered_map<std::string, std::string>& headers, curl_slist* list) 
    {
        std::for_each(headers.begin(), headers.end(), 
            [&](const std::unordered_map<std::string, std::string>::value_type& pair) {
                std::string header = pair.first+": "+pair.second;
                list = curl_slist_append(list, header.c_str());
            }
        );
        curl_easy_setopt(this->curlObj, CURLOPT_HTTPHEADER, list);
    }

    // exec http query
    bool HttpClient::exec(std::string& buffer) 
    {
        CURLcode codeResult = curl_easy_perform(this->curlObj);
        if(codeResult != CURLE_OK) {
            this->handleError(true, "Failed to perform query to [%s]", this->errorBuffer);
            return false;
        }
        else {
            return true;
        }
    }

    // get info struct data
    InfoStruct HttpClient::getInfo(std::string httpVerb) 
    {
        InfoStruct infos;
        infos.verb = httpVerb;

        curl_easy_getinfo(this->curlObj, CURLINFO_EFFECTIVE_URL, &(infos.host));
        curl_easy_getinfo(this->curlObj, CURLINFO_CONTENT_LENGTH_DOWNLOAD_T, &(infos.size));
        curl_easy_getinfo(this->curlObj, CURLINFO_RESPONSE_CODE, &(infos.status));
        curl_easy_getinfo(this->curlObj, CURLINFO_TOTAL_TIME, &(infos.time));

        return infos;
    }

    // handle any kind of error
    template<class... Args>
    void HttpClient::handleError(bool showError, std::string msg, Args... args) 
    {
        if(showError) {
            fprintf(stderr, msg.c_str(), args...);
            exit(EXIT_FAILURE);
        }
    }

    // clean curl context
    void HttpClient::clean(curl_slist* list)
    {
    	curl_easy_cleanup(this->curlObj);
    	curl_global_cleanup();
        curl_slist_free_all(list);       /* free the header list */
    }
}

#endif  // GRAPHQL_HTTP_HPP__