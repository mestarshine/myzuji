// 按键记录
export class KeyCount {
    all: number = 0;
    az: number = 0;
    number: number = 0;
    ctrl: number = 0;
    shift: number = 0;
    meta: number = 0;
    alt: number = 0;
    function: number = 0;
    space: number = 0;
    backspace: number = 0;
    semicolon: number = 0;
    quot: number = 0;

    reset(): void {
        this.all = 0;
        this.az = 0;
        this.number = 0;
        this.ctrl = 0;
        this.shift = 0;
        this.meta = 0;
        this.alt = 0;
        this.function = 0;
        this.space = 0;
        this.backspace = 0;
        this.semicolon = 0;
        this.quot = 0;
    }
}
