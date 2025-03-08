import { ARTICLE } from './constants';

export class Config {
    chapter: number; // 当前段号
    chapterTotal: number; // 总段数
    isShuffle: boolean; // 是否乱序模式
    count: number; // 单条数量
    articleName: string; // 文章名称
    article: string; // 文章内容
    darkMode: boolean; // 暗黑模式
    localStorageLabel: { [key: string]: string }; // 文章类型
    articleNameValue: string; // 文章标识
    isAutoNext: boolean; // 自动发文

    constructor() {
        this.chapter = 1;
        this.chapterTotal = 1;
        this.isShuffle = false;
        this.count = 15;
        this.articleName = ARTICLE.top500.name;
        this.article = ARTICLE.top500.content;
        this.darkMode = false;
        this.articleNameValue = ARTICLE.top500.value;
        this.isAutoNext = false;
        this.localStorageLabel = {
            chapter: 'type_pad_config_chapter',
            chapterTotal: 'type_pad_config_chapter_total',
            isShuffle: 'type_pad_config_is_shuffle',
            count: 'type_pad_config_count',
            articleName: 'type_pad_config_article_name',
            article: 'type_pad_config_article',
            darkMode: 'type_pad_config_dark_mode',
            articleNameValue: 'type_pad_config_article_identifier',
        };
    }

    // 判断是否存储过配置信息
    hasSavedData(): boolean {
        return Boolean(localStorage[this.localStorageLabel.articleNameValue]);
    }

    save(): void {
        localStorage[this.localStorageLabel.chapter] = this.chapter.toString();
        localStorage[this.localStorageLabel.chapterTotal] = this.chapterTotal.toString();
        localStorage[this.localStorageLabel.isShuffle] = this.isShuffle.toString();
        localStorage[this.localStorageLabel.count] = this.count.toString();
        localStorage[this.localStorageLabel.articleName] = this.articleName;
        localStorage[this.localStorageLabel.article] = this.article;
        localStorage[this.localStorageLabel.darkMode] = this.darkMode.toString();
        localStorage[this.localStorageLabel.articleNameValue] = this.articleNameValue;
    }

    get(): void {
        this.chapter = Number(localStorage[this.localStorageLabel.chapter]);
        this.chapterTotal = Number(localStorage[this.localStorageLabel.chapterTotal]);
        this.isShuffle = localStorage[this.localStorageLabel.isShuffle] === 'true';
        this.count = Number(localStorage[this.localStorageLabel.count]);
        this.articleName = localStorage[this.localStorageLabel.articleName] || '';
        this.article = localStorage[this.localStorageLabel.article] || '';
        this.darkMode = localStorage[this.localStorageLabel.darkMode] === 'true';
        this.articleNameValue = localStorage[this.localStorageLabel.articleNameValue] || '';
    }

    setWithCurrentConfig(): void {
        const shuffleMode = document.querySelector('#shuffleMode') as HTMLInputElement;
        if (shuffleMode) shuffleMode.checked = this.isShuffle;

        const radios = document.querySelectorAll<HTMLInputElement>('input[name=count]');
        radios.forEach((radio: HTMLInputElement) => {
            radio.checked = Number(radio.value) === this.count;
        });

        const articleSelect = document.querySelector('#article') as HTMLSelectElement;
        if (articleSelect) articleSelect.value = this.articleNameValue;

        const body = document.querySelector('body');
        if (body) {
            if (this.darkMode) {
                body.classList.add('black');
            } else {
                body.classList.remove('black');
            }
        }

        const darkModeButton = document.querySelector('#darkMode') as HTMLElement;
        if (darkModeButton) darkModeButton.innerText = this.darkMode ? '白色' : '暗黑';
    }
}
