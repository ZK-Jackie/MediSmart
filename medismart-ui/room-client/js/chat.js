$(document).ready(function() {
  var greenButton = $('#green');

  if (greenButton.length) {
      greenButton.click();
  }
});

const query = (obj) =>
  Object.keys(obj)
    .map((k) => encodeURIComponent(k) + "=" + encodeURIComponent(obj[k]))
    .join("&");
const colorThemes = $('[name="theme"]');
const markdown = window.markdownit();
const message_box = $('#messages');
const message_input = $('#message-input');
const box_conversations = $('.top');
const spinner = box_conversations.find(".spinner");
const stop_generating = $('.stop_generating');
const send_button = $('#send-button');
let prompt_lock = false;

hljs.addPlugin(new CopyButtonPlugin());

function resizeTextarea(textarea) {
  textarea = $(textarea);  // 将textarea转换为jQuery对象
  textarea.css('height', '80px');
  textarea.css('height', Math.min(textarea.prop('scrollHeight'), 200) + 'px');
}
const format = (text) => {
  return text.replace(/(?:\r\n|\r|\n)/g, "<br>");
};

message_input.on("blur", () => {
  $(window).scrollTop(0);
});

// message_input.on("focus", () => {
//   $('html, body').scrollTop($(document).height());
// });

const delete_conversations = async () => {
  localStorage.clear();
  await new_conversation();
};

const handle_ask = async () => {
  message_input.css('height', '80px');
  message_input.focus();

  $(window).scrollTop(0);
  let message = message_input.val();
  console.log("message: ", message);

  if (message.length > 0) {
    message_input.val('');
    console.log("message的length: ", message.length);
    await ask_gpt(message);
  }
};

const remove_cancel_button = async () => {
  stop_generating.addClass('stop_generating-hiding');

  setTimeout(() => {
    stop_generating.removeClass('stop_generating-hiding');
    stop_generating.addClass('stop_generating-hidden');
  }, 300);
};

const ask_gpt = async (message) => {
  try {
    message_input.val('');
    message_input.html('');
    message_input.text('');

    console.log("message_ask_gpt1: ", message);
    add_conversation(window.conversation_id, message.substr(0, 20));
    $(window).scrollTop(0);
    window.controller = new AbortController();

    let jailbreak = $("#jailbreak");
    let model = $("#model");
    prompt_lock = true;
    window.text = ``;
    window.token = message_id();

    stop_generating.removeClass(`stop_generating-hidden`);
    
    message_box.append(`
            <div class="message m1">
                <div class="user ur">
                    ${user_image}
                    
                </div>
                <div class="content u" id="user_${token}"> 
                    ${format(message)}
                </div>
            </div>
        `);

    message_box.scrollTop(message_box.prop('scrollHeight'));
    $(window).scrollTop(0);
    await new Promise((r) => setTimeout(r, 500));
    $(window).scrollTop(0);
    console.log("message_ask_gpt2: ", message);
    message_box.append(`
            <div class="message">
                <div class="user doctorr">
                    ${gpt_image} 
                   
                    

                </div>
                <div class="content doctor" id="gpt_${window.token}">
                    <div id="cursor"></div>
                </div>
            </div>
        `);

    message_box.scrollTop(message_box.prop('scrollHeight'));
    $(window).scrollTop(0);
    await new Promise((r) => setTimeout(r, 1000));
    $(window).scrollTop(0);
    console.log("message_ask_gpt3: ", message);
    let reqParam = {"input":message};
    console.log("reqParam: ", reqParam);
    // 创建一个新的URL对象
    let url = new URL(window.location.href);
    // 使用URLSearchParams对象获取GET请求参数
    let params = new URLSearchParams(url.search);
    // 获取特定的参数值
    let jwtToken = params.get('token'); // "value1"
    if(message) {
      console.log("message_ask_gpt4: ", message);
      $.ajax({
        url: 'http://localhost:8081/chat',
        type: 'POST',
        headers: {
          'content-type': 'application/json',
          'token': jwtToken
        },
        // headers: {
        //   'Content-Type': 'application/json',
        // },
        // data: JSON.stringify({
        //   conversation_id: window.conversation_id,
        //   action: '_ask',
        //   model: model.val(),
        //   jailbreak: jailbreak.val(),
        //   meta: {
        //     id: window.token,
        //     content: {
        //       conversation: await get_conversation(window.conversation_id),
        //       internet_access: $('#switch').prop('checked'),
        //       content_type: 'text',
        //       parts: [
        //         {
        //           content: message,
        //           role: 'user',
        //         },
        //       ],
        //     },
        //   },
        //
        data: JSON.stringify({ 
          content: message,
          conversationId: 10,
        }),
        // data : reqParam,
        success: function(response) {
          // 处理响应
          console.log(response);
          let text = response.data.output;
          console.log("message: ", message);
          console.log(markdown.render(text))
          $(`#gpt_${window.token}`).html(markdown.render(text));
          $('code').each(function() {
            hljs.highlightElement(this);
          });
  
          $(window).scrollTop(0);
          message_box.scrollTop(message_box.prop('scrollHeight'));
          if (
            text.includes(
              `instead. Maintaining this website and API costs a lot of money`
            )
          ) {
            $(`#gpt_${window.token}`).html("An error occured, please reload / refresh cache and try again.");
          }
          
          add_message(window.conversation_id, "user", message);
          add_message(window.conversation_id, "assistant", text);
          
          message_box.scrollTop(message_box.prop('scrollHeight'));
          remove_cancel_button();
          prompt_lock = false;
          
          load_conversations(20, 0);
          $(window).scrollTop(0);
        },
        error: function(error) {
          // 处理错误
          console.log(error);
        }
      });
    }else {
      console.log("message_ask_gpt5: ", message);
    }
  } catch (e) {
    add_message(window.conversation_id, "user", message);

    message_box.scrollTop(message_box.prop('scrollHeight'));
    await remove_cancel_button();
    prompt_lock = false;

    await load_conversations(20, 0);

    console.log(e);

    let cursorDiv = $('#cursor');
    if (cursorDiv.length) cursorDiv.remove();

    if (e.name != 'AbortError') {
      let error_message = 'oops ! something went wrong, please try again / reload. [stacktrace in console]';

      $(`#gpt_${window.token}`).html(error_message);
      add_message(window.conversation_id, "assistant", error_message);
    } else {
      $(`#gpt_${window.token}`).html(' [aborted]');
      add_message(window.conversation_id, "assistant", text + ' [aborted]');
    }

    $(window).scrollTop(0);
  }
};

const clear_conversations = async () => {
  box_conversations.children().each(function() {
    if (this.nodeType === Node.ELEMENT_NODE && this.tagName.toLowerCase() !== 'button') {
      $(this).remove();
    }
  });
};

const clear_conversation = async () => {
  message_box.find('div').remove();
};

const show_option = async (conversation_id) => {
  const conv = $(`#conv-${conversation_id}`);
  const yes = $(`#yes-${conversation_id}`);
  const not = $(`#not-${conversation_id}`);

  conv.css('display', 'none');
  yes.css('display', 'block');
  not.css('display', 'block');
}
const hide_option = async (conversation_id) => {
  const conv = $(`#conv-${conversation_id}`);
  const yes = $(`#yes-${conversation_id}`);
  const not = $(`#not-${conversation_id}`);

  conv.css('display', 'block');
  yes.css('display', 'none');
  not.css('display', 'none');
}

const delete_conversation = async (conversation_id) => {
  localStorage.removeItem(`conversation:${conversation_id}`);

  const conversation = $(`#convo-${conversation_id}`);
  conversation.remove();

  if (window.conversation_id == conversation_id) {
    await new_conversation();
  }

  await load_conversations(20, 0, true);
};
const set_conversation = async (conversation_id) => {
  // history.pushState({}, null, `/chat/${conversation_id}`);
  // window.conversation_id = $(`#${conversation_id}`);
  //
  // await clear_conversation();
  // await load_conversation(conversation_id);
  // await load_conversations(20, 0, true);
};

const new_conversation = async () => {
  history.pushState({}, null, `/chat/`);
  window.conversation_id = uuid();

  await clear_conversation();
  await load_conversations(20, 0, true);
};

const load_conversation = async (conversation_id) => {
  let conversation = await JSON.parse(
    localStorage.getItem(`conversation:${conversation_id}`)
  );
  console.log(conversation, conversation_id);

  for (item of conversation.items) {
    message_box.append(`
            <div class="message">
                <div class="user ">
                    ${item.role == "assistant" ? gpt_image : user_image}
                    
                </div>
                <div class="content">
                    ${
                      item.role == "assistant"
                        ? markdown.render(item.content)
                        : item.content
                    }
                </div>
            </div>
        `);
  }

  $('code').each(function() {
    hljs.highlightElement(this);
  });

  message_box.animate({ scrollTop: message_box.prop('scrollHeight') }, 'slow');

  setTimeout(() => {
    message_box.scrollTop(message_box.prop('scrollHeight'));
  }, 500);
};

const get_conversation = async (conversation_id) => {
  let conversation = await JSON.parse(
    localStorage.getItem(`conversation:${conversation_id}`)
  );
  return conversation.items;
};

const add_conversation = async (conversation_id, title) => {
  if (localStorage.getItem(`conversation:${conversation_id}`) == null) {
    localStorage.setItem(
      `conversation:${conversation_id}`,
      JSON.stringify({
        id: conversation_id,
        title: title,
        items: [],
      })
    );
  }
};

const add_message = async (conversation_id, role, content) => {
  before_adding = JSON.parse(
    localStorage.getItem(`conversation:${conversation_id}`)
  );

  before_adding.items.push({
    role: role,
    content: content,
  });

  localStorage.setItem(
    `conversation:${conversation_id}`,
    JSON.stringify(before_adding)
  ); // update conversation
};

const load_conversations = async (limit, offset, loader) => {
  let conversations = [];
  for (let i = 0; i < localStorage.length; i++) {
    if (localStorage.key(i).startsWith("conversation:")) {
      let conversation = localStorage.getItem(localStorage.key(i));
      conversations.push(JSON.parse(conversation));
    }
  }

  await clear_conversations();

  for (conversation of conversations) {
    // box_conversations.append(`
    // <div class="convo" id="convo-${conversation.id}">
    //   <div class="left" onclick="set_conversation('${conversation.id}')">
    //   <i class="fa-thin fa-hand-holding-medical"></i>
    //       <span class="convo-title">${conversation.title}</span>
    //   </div>
    //   <i onclick="show_option('${conversation.id}')" class="fa-regular fa-trash" id="conv-${conversation.id}"></i>
    //   <i onclick="delete_conversation('${conversation.id}')" class="fa-regular fa-check" id="yes-${conversation.id}" style="display:none;"></i>
    //   <i onclick="hide_option('${conversation.id}')" class="fa-regular fa-x" id="not-${conversation.id}" style="display:none;"></i>
    // </div>
    // `);
    box_conversations.append(`
    <div class="convo" id="convo-${conversation.id}">
      <div class="left" onclick="set_conversation('${conversation.id}')">
      <i class="fa-thin fa-hand-holding-medical"></i>
          <span class="convo-title">${conversation.title}</span>
      </div>
    </div>
    `);
  }

  $('code').each(function() {
    hljs.highlightElement(this);
  });
};

$('#cancelButton').on('click', async () => {
  window.controller.abort();
  console.log(`aborted ${window.conversation_id}`);
});

function h2a(str1) {
  var hex = str1.toString();
  var str = "";

  for (var n = 0; n < hex.length; n += 2) {
    str += String.fromCharCode(parseInt(hex.substr(n, 2), 16));
  }

  return str;
}

const uuid = () => {
  return `xxxxxxxx-xxxx-4xxx-yxxx-${Date.now().toString(16)}`.replace(
    /[xy]/g,
    function (c) {
      var r = (Math.random() * 16) | 0,
        v = c == "x" ? r : (r & 0x3) | 0x8;
      return v.toString(16);
    }
  );
};

const message_id = () => {
  random_bytes = (Math.floor(Math.random() * 1338377565) + 2956589730).toString(
    2
  );
  unix = Math.floor(Date.now() / 1000).toString(2);

  return BigInt(`0b${unix}${random_bytes}`).toString();
};

window.onload = async () => {
  load_settings_localstorage();

  conversations = 0;
  for (let i = 0; i < localStorage.length; i++) {
    if (localStorage.key(i).startsWith("conversation:")) {
      conversations += 1;
    }
  }

  if (conversations == 0) localStorage.clear();

  await setTimeout(() => {
    load_conversations(20, 0);
  }, 1);

  if (!window.location.href.endsWith(`#`)) {
    if (/\/chat\/.+/.test(window.location.href)) {
      await load_conversation(window.conversation_id);
    }
  }

message_input.on('keydown', async (evt) => {
  if (prompt_lock) return;
  if (evt.keyCode === 13 && !evt.shiftKey) {
    evt.preventDefault();
    console.log('pressed enter');
    await handle_ask();
  } else {
    message_input.css('height', '');
    message_input.css('height', message_input.prop('scrollHeight') + 4 + 'px');
  }
});

send_button.on('click', async () => {
  console.log("clicked send");
  if (prompt_lock) return;
  await handle_ask();
});

register_settings_localstorage();
};
$(".mobile-sidebar").on("click", function() {
  const sidebar = $(".conversations");

  if (sidebar.hasClass("shown")) {
    sidebar.removeClass("shown");
    $(this).removeClass("rotated");
  } else {
    sidebar.addClass("shown");
    $(this).addClass("rotated");
  }

  $(window).scrollTop(0);
});

const register_settings_localstorage = async () => {
  const settings_ids = ["switch", "model", "jailbreak"];

  settings_ids.forEach((id) => {
    const element = $(`#${id}`);

    element.on('change', function(event) {
      switch (this.type) {
        case "checkbox":
          localStorage.setItem(this.id, this.checked);
          break;
        case "select-one":
          localStorage.setItem(this.id, this.selectedIndex);
          break;
        default:
          console.warn("Unresolved element type");
      }
    });
  });
};

const load_settings_localstorage = async () => {
  const settings_ids = ["switch", "model", "jailbreak"];

  settings_ids.forEach((id) => {
    const element = $(`#${id}`);

    if (localStorage.getItem(id)) {
      switch (element.prop('type')) {
        case "checkbox":
          element.prop('checked', localStorage.getItem(id) === "true");
          break;
        case "select-one":
          element.prop('selectedIndex', parseInt(localStorage.getItem(id)));
          break;
        default:
          console.warn("Unresolved element type");
      }
    }
  });
};

// Theme storage for recurring viewers
const storeTheme = function (theme) {
  localStorage.setItem("theme", theme);
};

// set theme when visitor returns
const setTheme = function () {
  const activeTheme = localStorage.getItem("theme");
  colorThemes.each(function() {
    if (this.id === activeTheme) {
      $(this).prop('checked', true);
    }
  });
  // fallback for no :has() support
  $('html').attr('class', activeTheme);
};

colorThemes.each(function() {
  $(this).on("click", function() {
    storeTheme(this.id);
    // fallback for no :has() support
    $('html').attr('class', this.id);
  });
});

document.onload = setTheme();


