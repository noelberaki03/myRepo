#include <iostream>
#include <vector>

void merge(std::vector<int> &left, std::vector<int> &right, std::vector<int> &vect);

void mergeSort(std::vector<int> &vect) {
    if (vect.size() < 2) {
        return;
    }

    int mid = vect.size() / 2;
    std::vector<int> leftSide;
    std::vector<int> rightSide;

    for (int i = 0; i < mid; i++) {
        leftSide.push_back(vect[i]);
    }
    for (int i = mid; i < vect.size(); i++) {
        rightSide.push_back(vect[i]);
    }

    mergeSort(leftSide);
    mergeSort(rightSide);
    merge(leftSide, rightSide, vect);
}

void merge(std::vector<int> &left, std::vector<int> &right, std::vector<int> &vect) {
    int i = 0;
    int j = 0;
    vect.clear();

    while (i < left.size() && j < right.size()) {
        if (left[i] <= right[j]) {
            vect.push_back(left[i]);
            i++;
        }
        else {
            vect.push_back(right[j]);
            j++;
        }
    }

    while (i < left.size()) {
        vect.push_back(left[i]);
        i++;
    }

    while (j < right.size()) {
        vect.push_back(right[j]);
        j++;
    }
}

int main() {
    std::vector<int> vect;
    
    vect.push_back(40);
    vect.push_back(7);
    vect.push_back(3);
    vect.push_back(10);
    vect.push_back(24);
    vect.push_back(13);
    vect.push_back(100);
    vect.push_back(88);
    vect.push_back(35);
    vect.push_back(20);

    std::cout << "UNSORTED:\n";
    for (int i : vect) {
        std::cout << i << " ";
    }

    mergeSort(vect);
    std::cout << "\n\nSORTED:\n";
    for (int i : vect) {
        std::cout << i << " ";
    }

    std::cout << "\n\n";
    return 0;
}