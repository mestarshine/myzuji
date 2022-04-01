package com.myzuji.study.java.lang;

/**
 * java.lang.Package
 *
 * @author shine
 * @date 2020/03/11
 */
public class PackageStudy {

    public static void main(String[] args) {
        Package[] packages = Package.getPackages();
        for (int i = 0; i < packages.length; i++) {
            System.out.println("package name：" + packages[i].getName());
            System.out.println("Implementation Title：" + packages[i].getImplementationTitle());
            System.out.println("Implementation Vendor：" + packages[i].getImplementationVendor());
            System.out.println("Implementation Version：" + packages[i].getImplementationVersion());
            System.out.println("Specification Title：" + packages[i].getSpecificationTitle());
            System.out.println("Specification Vendor：" + packages[i].getSpecificationVendor());
            System.out.println("Specification Version：" + packages[i].getSpecificationVersion());
            System.out.println("================");
        }
    }
}
