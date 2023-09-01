#include <vector>
#include <iostream>

void reverse(std::vector<int> &vect) {
    for (int i = 0; i < vect.size() / 2; i++) {
        int temp = vect[i];
        vect[i] = vect[vect.size() - 1 - i];
        vect[vect.size() - 1 - i] = temp;
    }
}

int main() {
    std::vector<int> vect;
    vect.push_back(1);
    vect.push_back(2);
    vect.push_back(3);
    vect.push_back(4);
    vect.push_back(5);


    for (int i : vect) {
        std::cout << i << " ";
    }
    std::cout << "\n";
    reverse(vect);
    for (int i : vect) {
        std::cout << i << " ";
    }

    return 0;
}