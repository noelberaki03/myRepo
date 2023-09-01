#include <iostream>
#include <vector>

void merge(std::vector<int> &left, std::vector<int> &right, std::vector<int> &vect);

int binarySearch(std::vector<int> &vect, int &key);
int binarySearchHelper(std::vector<int> &vect, int &key, int &first, int &last);

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

int binarySearch(std::vector<int> &vect, int &key) {
    int first = 0;
    int last = vect.size() - 1;
    return binarySearchHelper(vect, key, first, last);
}

int binarySearchHelper(std::vector<int> &vect, int &key, int &first, int &last) {
    if (first > last) {
        return -1;
    }
    else {
        int mid = (first + last) / 2;
        if (vect[mid] == key) {
            return mid;
        }
        else if (vect[mid] > key) {
            last = mid - 1;
            return binarySearchHelper(vect, key, first, last);
        }
        else {
            first = mid + 1;
            return binarySearchHelper(vect, key, first, last);
        }
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

    std::cout << "\n\nUNSORTED:\n";
    for (int i : vect) {
        std::cout << i << " ";
    }

    mergeSort(vect);
    std::cout << "\n\nSORTED:\n";
    for (int i : vect) {
        std::cout << i << " ";
    }

    int num;
    std::cout << "\n\nEnter a number: ";
    std::cin >> num;

    int index = binarySearch(vect, num);

    if (index >= 0) {
        std::cout << num << " found at index: " << index << "\n\n\n";
    }
    else {
        std::cout << num << " not in the list.\n\n\n";
    }
    return 0;
}