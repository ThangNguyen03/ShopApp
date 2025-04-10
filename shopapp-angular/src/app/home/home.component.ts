import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../base/base.component';
import { Product } from '../models/product';
import { Category } from '../models/category';
import { environment } from 'src/environments/environment';
import { ApiResponse } from '../responses/api.response';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent extends BaseComponent implements OnInit {
  products: Product[] = [];
  categories: Category[] = []; // Dữ liệu động từ categoryService
  selectedCategoryId: number = 0; // Giá trị category được chọn
  currentPage: number = 0;
  itemsPerPage: number = 5;
  pages: number[] = [];
  totalPages: number = 0;
  visiblePages: number[] = [];
  keyword: string = "";
  localStorage?: Storage | undefined;
  apiBaseUrl = environment.apiBaseUrl;
  

  ngOnInit() {
    this.activatedRoute.queryParams.subscribe(params => {
      this.currentPage = Number(params['page']) || 0;
      this.keyword = params['keyword'] || '';
      this.selectedCategoryId = Number(params['categoryId']) || 0;
    });
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    this.getCategories(0, 100);
  }  

  constructor() {
    super();
    this.localStorage = this.document.defaultView?.localStorage;
  }

  getCategories(page: number, limit: number) {
    this.categoryService.getCategories(page, limit).subscribe({
      // next: (apiResponse: ApiResponse) => {
      //   this.categories = apiResponse.data;
      next: (apiResponse: any) => {
        this.categories = apiResponse;
      },
      complete: () => {
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi tải danh sách sản phẩm',
          title: 'Lỗi Tải Dữ Liệu'
        });
      }
    });
  }

  searchProducts() {
    this.currentPage = 0;
    this.itemsPerPage = 12;
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    this.router.navigate(['/home'], { 
      queryParams: 
      { keyword: this.keyword, categoryId: this.selectedCategoryId, page: this.currentPage } 
    });
  }  

  getProducts(keyword: string, selectedCategoryId: number, page: number, limit: number) {
    this.productService.getProducts(keyword, selectedCategoryId, page, limit).subscribe({
      // next: (apiresponse: ApiResponse) => {
      next: (apiresponse: any) => {
        const response = apiresponse;
        response.products.forEach((product: Product) => {
          product.url = `${environment.apiBaseUrl}/products/images/${product.thumbnail}`;
        });
        this.products = response.products;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
      },
      error: (error: HttpErrorResponse) => {
        this.toastService.showToast({
          error: error,
          defaultMsg: 'Lỗi tải danh sách sản phẩm',
          title: 'Lỗi Tải Dữ Liệu'
        });
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage = page < 0 ? 0 : page;
    this.getProducts(this.keyword, this.selectedCategoryId, this.currentPage, this.itemsPerPage);
    this.router.navigate(['/home'], { queryParams: { page: this.currentPage } });
  }  
  
  // Hàm xử lý sự kiện khi sản phẩm được bấm vào
  onProductClick(productId: number) {
    // Điều hướng đến trang detail-product với productId là tham số
    this.router.navigate(['/products', productId]);
  }
  trackByProductId(index: number, item: any): number {
    return item.id;
  }
  
  trackByCategoryName(index: number, item: any): string {
    return item.name;
  }
  
  trackByPage(index: number, item: number): number {
    return item;
  }
  
}
