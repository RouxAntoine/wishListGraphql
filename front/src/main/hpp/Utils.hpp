#include <string>
#include <cstring>

namespace GraphQL::Utils
{

    // writer use by curlcpp to write data to string
    static int writer(char *data, size_t size, size_t nmemb, std::string *writerData)
    {
        if(writerData == nullptr) {
            return 0;
        }

        writerData->append(data, size*nmemb);
        return size * nmemb;
    }

    //
    static size_t reader(char** dest, size_t size, size_t nmemb, void* userp)
    {
        if(userp == nullptr) {
            return 0;
        }
        char* wt = ((char*)((std::string*) userp)->c_str());
        std::memcpy(dest, wt, std::strlen(wt));
        return std::strlen(wt);            /* we copied this amount of bytes */ 
    }

    // std::string replaceAll(std::string& str, const std::string& from, const std::string& to) {
    //     if(from.empty())
    //         return "";
    //     size_t start_pos = 0;
    //     while((start_pos = str.find(from, start_pos)) != std::string::npos) {
    //         str.replace(start_pos, from.length(), to);
    //         start_pos += to.length(); // In case 'to' contains 'from', like replacing 'x' with 'yx'
    //     }
    //     return str;
    // }

}